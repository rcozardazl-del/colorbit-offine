package com.example.data

import com.example.model.ComponentType
import com.example.model.MyP2PSaleListing
import com.example.model.P2PListing
import com.example.model.P2PSwarmState
import com.example.model.PCComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.UUID
import kotlin.random.Random

class P2PAvitoService(private val scope: CoroutineScope) {

    private val myPeerId = "peer_0x" + UUID.randomUUID().toString().substring(0, 6)

    private val _swarmState = MutableStateFlow(
        P2PSwarmState(
            isConnected = true,
            myPeerId = myPeerId,
            myPeerName = "Вы (Майнер-$myPeerId)",
            peersCount = 24,
            averagePingMs = 22,
            networkMode = "P2P Mesh / UDP & DHT Swarm",
            localSubnetIp = "192.168.1." + Random.nextInt(10, 240),
            isScanning = false
        )
    )
    val swarmState: StateFlow<P2PSwarmState> = _swarmState.asStateFlow()

    private val _listings = MutableStateFlow<List<P2PListing>>(emptyList())
    val listings: StateFlow<List<P2PListing>> = _listings.asStateFlow()

    private val _mySales = MutableStateFlow<List<MyP2PSaleListing>>(emptyList())
    val mySales: StateFlow<List<MyP2PSaleListing>> = _mySales.asStateFlow()

    private val peerNames = listOf(
        "@CryptoBrat_SPB", "@Miner_Vovan77", "@SatsStacker", "@Hardware_Ghost",
        "@CyberFarm_Novosib", "@BitMaster_Ekb", "@GarageRig_Vitek", "@GpuCollector",
        "@Overclock_Pro", "@BtcNomad", "@P2P_Escrow_Whale", "@Dmitriy_Mining",
        "@EthMaxi_Kazan", "@RigFixer_99", "@Alex_HashRate", "@Stas_Silicon"
    )

    private val peerClaims = listOf(
        "Не бита, не крашена, пломбы на месте",
        "Стояла в офисном ПК у бухгалтера",
        "Использовалась чисто под CS:GO и браузер",
        "Майнила только по праздникам с даунвольтом",
        "Без проверок, снята со старого рига",
        "Идеальное состояние, пылинки сдувал",
        "Вроде рабочая, вертушка немного шуршит",
        "Стояла на балконе, температуры ледяные",
        "Продаю как есть, манибэка нет",
        "Состояние пушка, любые тесты FurMark",
        "Домашний ПК, пломбы не срывались",
        "Память Samsung, не грелась (честно!)"
    )

    private val peerComments = listOf(
        "Стояла в сухом помещении, даунвольт 65%, вертушки в норме.",
        "Срочно нужны USD на оплату аренды розетки!",
        "Память Samsung! Разгоняется отлично, держала стабильно.",
        "Куплена год назад, пломбы на месте, термопаста свежая.",
        "Распродаю риг из-за переезда, отдаю через P2P Escrow.",
        "С домашнего ПК, майнила только по ночам, без перегревов.",
        "Блок питания с сертификатом 80+ Gold, чистые 12V без просадок.",
        "Использовалась в продуваемом каркасе, температуры в норме."
    )

    init {
        generateInitialListings()
        startLanPeerDiscovery()
        startAutoSalesBuyerSimulation()
    }

    private fun generateInitialListings() {
        val baseComponents = ComponentCatalog.allComponents
        val generated = mutableListOf<P2PListing>()

        // Создаём ~14 разнообразных P2P лотов от пиров (игра-угадайка: реальное состояние скрыто!)
        baseComponents.forEachIndexed { index, comp ->
            // Б/у цена (без скидок, фиксированная рыночная цена продавца)
            val priceFactor = Random.nextDouble(0.65, 0.85)
            // Реальное скрытое состояние: от ужаренной (20-40%) до идеальной (85-98%)
            val realCondition = Random.nextInt(20, 96).toFloat()
            val peerIndex = index % peerNames.size
            val sellerName = peerNames[peerIndex]
            val peerId = "peer_0x" + Integer.toHexString(sellerName.hashCode()).takeLast(4)
            val p2pPrice = (comp.priceUsd * priceFactor).coerceAtLeast(15.0)
            val claim = peerClaims[index % peerClaims.size]

            val usedComp = comp.copy(
                id = "p2p_${comp.id}_$index",
                name = comp.name,
                priceUsd = p2pPrice,
                isUsed = true,
                baseDurability = realCondition,
                description = "P2P лот от $sellerName. Продавец: «$claim»"
            )

            generated.add(
                P2PListing(
                    id = "p2p_item_${UUID.randomUUID().toString().take(8)}",
                    component = usedComp,
                    sellerPeerId = peerId,
                    sellerName = sellerName,
                    sellerRating = Random.nextDouble(4.6, 5.0).let { "%.1f".format(it).replace(',', '.').toFloat() },
                    successfulDeals = Random.nextInt(12, 85),
                    pingMs = Random.nextInt(12, 48),
                    conditionPercent = realCondition, // Скрыто от покупателя!
                    sellerClaim = claim,
                    sellerComment = peerComments[index % peerComments.size],
                    priceUsd = p2pPrice
                )
            )
        }

        _listings.value = generated.shuffled()
    }

    fun scanNetwork(onCompleted: () -> Unit = {}) {
        scope.launch {
            _swarmState.value = _swarmState.value.copy(isScanning = true)
            delay(1200) // имитация P2P DHT сканирования пиров

            val newPeers = Random.nextInt(20, 35)
            val newPing = Random.nextInt(14, 28)
            _swarmState.value = _swarmState.value.copy(
                peersCount = newPeers,
                averagePingMs = newPing,
                isScanning = false
            )

            // Добавляем 2-3 новых лота от вновь найденных пиров
            val current = _listings.value.filter { !it.isSold }.toMutableList()
            val randomComp = ComponentCatalog.allComponents.random()
            val factor = Random.nextDouble(0.60, 0.82)
            val realCond = Random.nextInt(20, 96).toFloat()
            val seller = peerNames.random()
            val claim = peerClaims.random()
            val freshP2P = P2PListing(
                id = "p2p_fresh_${System.currentTimeMillis()}",
                component = randomComp.copy(
                    id = "p2p_fresh_comp_${System.currentTimeMillis()}",
                    priceUsd = randomComp.priceUsd * factor,
                    isUsed = true,
                    baseDurability = realCond,
                    description = "P2P лот от $seller. Продавец: «$claim»"
                ),
                sellerPeerId = "peer_0x" + Random.nextInt(1000, 9999),
                sellerName = seller,
                sellerRating = 4.9f,
                successfulDeals = Random.nextInt(20, 60),
                pingMs = Random.nextInt(15, 35),
                conditionPercent = realCond,
                sellerClaim = claim,
                sellerComment = "Только что выложил на Авито P2P! Забирайте пока не ушла.",
                priceUsd = randomComp.priceUsd * factor
            )
            current.add(0, freshP2P)
            _listings.value = current.take(25)
            onCompleted()
        }
    }

    fun markListingAsSold(listingId: String) {
        _listings.value = _listings.value.map {
            if (it.id == listingId) it.copy(isSold = true) else it
        }
    }

    fun publishPlayerComponent(
        component: PCComponent,
        rigId: String,
        slotId: String,
        durability: Float,
        askingPrice: Double
    ): MyP2PSaleListing {
        val newSale = MyP2PSaleListing(
            id = "sale_${System.currentTimeMillis()}",
            component = component,
            rigId = rigId,
            slotId = slotId,
            durabilityPercent = durability,
            askingPriceUsd = askingPrice,
            createdAtTimestamp = System.currentTimeMillis()
        )
        _mySales.value = listOf(newSale) + _mySales.value
        return newSale
    }

    fun removePlayerListing(saleId: String) {
        _mySales.value = _mySales.value.filter { it.id != saleId }
    }

    // Симуляция покупок выставленных игроком деталей другими пирами
    private fun startAutoSalesBuyerSimulation() {
        scope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(7000) // каждые 7 секунд проверяем активные объявления игрока
                val activeSales = _mySales.value.filter { !it.isSold }
                if (activeSales.isNotEmpty()) {
                    val saleToBuy = activeSales.random()
                    val buyer = peerNames.random()
                    // Пир выкупает лот
                    _mySales.value = _mySales.value.map {
                        if (it.id == saleToBuy.id) {
                            it.copy(isSold = true, buyerPeerName = buyer)
                        } else it
                    }
                }
            }
        }
    }

    // Реальный LAN UDP Broadcast для поиска пиров в одной локальной сети / Wi-Fi
    private fun startLanPeerDiscovery() {
        scope.launch(Dispatchers.IO) {
            try {
                val socket = DatagramSocket(18888).apply {
                    broadcast = true
                    soTimeout = 4000
                }
                val buffer = ByteArray(256)

                // Поток слушателя
                while (isActive) {
                    try {
                        val packet = DatagramPacket(buffer, buffer.size)
                        socket.receive(packet)
                        val message = String(packet.data, 0, packet.length)
                        if (message.startsWith("COLORBIT_P2P:") && !message.contains(myPeerId)) {
                            // Обнаружен реальный пир в локальной сети!
                            _swarmState.value = _swarmState.value.copy(
                                peersCount = _swarmState.value.peersCount + 1,
                                averagePingMs = 8 // локальная сеть даёт низкий пинг
                            )
                        }
                    } catch (_: Exception) {
                        // Таймаут сокета — нормально, продолжаем
                    }

                    // Периодически отправляем анонс своего пира
                    try {
                        val broadcastMsg = "COLORBIT_P2P:$myPeerId"
                        val sendData = broadcastMsg.toByteArray()
                        val broadcastAddress = InetAddress.getByName("255.255.255.255")
                        val sendPacket = DatagramPacket(sendData, sendData.size, broadcastAddress, 18888)
                        socket.send(sendPacket)
                    } catch (_: Exception) {
                    }

                    delay(5000)
                }
            } catch (_: Exception) {
                // Если сокет не может открыться (порт занят или нет прав), продолжаем работать через DHT P2P симуляцию
            }
        }
    }
}
