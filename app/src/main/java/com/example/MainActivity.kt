package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.ColorbitViewModel
import com.example.ui.GameTab
import com.example.ui.screens.ExchangeScreen
import com.example.ui.screens.FacilitiesScreen
import com.example.ui.screens.OnlineWebViewScreen
import com.example.ui.screens.QuestsScreen
import com.example.ui.screens.RigsScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.screens.StoryDialogueDialog
import com.example.ui.theme.DarkCyberBorder
import com.example.ui.theme.DarkCyberCard
import com.example.ui.theme.DarkCyberCardElevated
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ColorbitApp()
            }
        }
    }
}

@Composable
fun ColorbitApp(vm: ColorbitViewModel = viewModel()) {
    var isOnlineMode by remember { mutableStateOf(false) }

    val selectedTab by vm.selectedTab.collectAsState()
    val stats by vm.playerStats.collectAsState()
    val rigs by vm.rigs.collectAsState()
    val cryptos by vm.cryptos.collectAsState()
    val facilities by vm.facilities.collectAsState()
    val quests by vm.quests.collectAsState()
    val storyChapters by vm.storyChapters.collectAsState()
    val activeStoryDialogue by vm.activeStoryDialogue.collectAsState()
    val loans by vm.loans.collectAsState()
    val p2pSwarm by vm.p2pSwarmState.collectAsState()
    val p2pListings by vm.p2pListings.collectAsState()
    val myP2PSales by vm.myP2PSales.collectAsState()
    val notification by vm.statusNotification.collectAsState()
    val offlineEarnings by vm.offlineDialogInfo.collectAsState()

    if (isOnlineMode) {
        OnlineWebViewScreen(
            onSwitchToOffline = { isOnlineMode = false }
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                // Верхняя панель: Название + баланс наличных в USD + переключатель режима
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .background(DarkCyberCardElevated)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "COLOR",
                                color = NeonCyan,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                "BIT",
                                color = NeonGreen,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NeonGreen.copy(alpha = 0.2f))
                                    .clickable { isOnlineMode = true }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("ОФЛАЙН", color = NeonGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Кнопка перехода в онлайн colorbit.ru
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonCyan.copy(alpha = 0.15f))
                                    .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .clickable { isOnlineMode = true }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Language,
                                        contentDescription = "Онлайн",
                                        tint = NeonCyan,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Онлайн", color = NeonCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Баланс USD
                            Card(
                                colors = CardDefaults.cardColors(containerColor = DarkCyberCard),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCyberBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "$%.2f".format(stats.balanceUsd),
                                        color = if (stats.balanceUsd >= 0) NeonGreen else Color(0xFFFF5252),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            },
            bottomBar = {
                // Нижняя навигация
                NavigationBar(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                    containerColor = DarkCyberCardElevated,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = selectedTab == GameTab.RIGS,
                        onClick = { vm.selectTab(GameTab.RIGS) },
                        icon = { Icon(Icons.Default.Memory, contentDescription = "Фермы") },
                        label = { Text("Риги") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            indicatorColor = NeonGreen,
                            unselectedIconColor = TextMuted,
                            selectedTextColor = NeonGreen,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_rigs")
                    )
                    NavigationBarItem(
                        selected = selectedTab == GameTab.SHOP,
                        onClick = { vm.selectTab(GameTab.SHOP) },
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Магазин") },
                        label = { Text("Магазин") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            indicatorColor = NeonGreen,
                            unselectedIconColor = TextMuted,
                            selectedTextColor = NeonGreen,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_shop")
                    )
                    NavigationBarItem(
                        selected = selectedTab == GameTab.EXCHANGE,
                        onClick = { vm.selectTab(GameTab.EXCHANGE) },
                        icon = { Icon(Icons.Default.CurrencyExchange, contentDescription = "Биржа") },
                        label = { Text("Биржа") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            indicatorColor = NeonGreen,
                            unselectedIconColor = TextMuted,
                            selectedTextColor = NeonGreen,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_exchange")
                    )
                    NavigationBarItem(
                        selected = selectedTab == GameTab.FACILITIES,
                        onClick = { vm.selectTab(GameTab.FACILITIES) },
                        icon = { Icon(Icons.Default.LocationCity, contentDescription = "Локации") },
                        label = { Text("ДомКлик") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            indicatorColor = NeonGreen,
                            unselectedIconColor = TextMuted,
                            selectedTextColor = NeonGreen,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_facilities")
                    )
                    NavigationBarItem(
                        selected = selectedTab == GameTab.QUESTS,
                        onClick = { vm.selectTab(GameTab.QUESTS) },
                        icon = { Icon(Icons.Default.Assignment, contentDescription = "Сюжет") },
                        label = { Text("Сюжет") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            indicatorColor = NeonGreen,
                            unselectedIconColor = TextMuted,
                            selectedTextColor = NeonGreen,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_quests")
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    GameTab.RIGS -> RigsScreen(
                        rigs = rigs,
                        cryptos = cryptos,
                        onTogglePower = { vm.toggleRigPower(it) },
                        onSwitchCoin = { rigId, coinId -> vm.switchRigCoin(rigId, coinId) },
                        onCleanDust = { vm.cleanDust(it) },
                        onReplacePaste = { vm.replaceThermalPaste(it) },
                        onAddNewRig = { vm.addNewRig(it) },
                        onApplyOverclock = { rigId, core, mem, pl, fan ->
                            vm.applyOverclock(rigId, core, mem, pl, fan)
                        }
                    )
                    GameTab.SHOP -> ShopScreen(
                        balanceUsd = stats.balanceUsd,
                        activeDebtUsd = stats.activeDebtUsd,
                        rigs = rigs,
                        loans = loans,
                        p2pSwarm = p2pSwarm,
                        p2pListings = p2pListings,
                        myP2PSales = myP2PSales,
                        onBuyComponent = { rigId, comp -> vm.buyAndInstallComponent(rigId, comp) },
                        onTakeLoan = { vm.takeLoan(it) },
                        onRepayLoan = { vm.repayLoan(it) },
                        onScanP2P = { vm.scanP2PNetwork() },
                        onBuyP2PListing = { rigId, listing -> vm.buyP2PListing(rigId, listing) },
                        onSellComponentToP2P = { rigId, slotId, price -> vm.sellComponentToP2P(rigId, slotId, price) },
                        onClaimSoldPayment = { saleId -> vm.claimSoldP2PPayment(saleId) },
                        onInstantSellScrap = { rigId, slotId -> vm.instantSellToScrapPeer(rigId, slotId) }
                    )
                    GameTab.EXCHANGE -> ExchangeScreen(
                        cryptos = cryptos,
                        balances = stats.cryptoBalances,
                        onSellCrypto = { coinId, amt -> vm.sellCrypto(coinId, amt) }
                    )
                    GameTab.FACILITIES -> FacilitiesScreen(
                        facilities = facilities,
                        balanceUsd = stats.balanceUsd,
                        currentRigsCount = rigs.size,
                        onRentFacility = { id, period -> vm.rentFacility(id, period) },
                        onSetActiveFacility = { id -> vm.setActiveFacility(id) },
                        onUnlockFacility = { vm.unlockFacility(it) }
                    )
                    GameTab.QUESTS -> QuestsScreen(
                        quests = quests,
                        stats = stats,
                        storyChapters = storyChapters,
                        onClaimReward = { vm.claimQuestReward(it) },
                        onOpenStoryDialogue = { vm.showStoryDialogue(it) },
                        onClaimChapterReward = { vm.completeChapter(it) }
                    )
                }

                // Всплывающее уведомление о действиях
                AnimatedVisibility(
                    visible = notification != null,
                    enter = slideInVertically(initialOffsetY = { -it }),
                    exit = slideOutVertically(targetOffsetY = { -it }),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(notification ?: "", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Диалог сюжетного диалога (катсцена)
        if (activeStoryDialogue != null) {
            StoryDialogueDialog(
                chapter = activeStoryDialogue!!,
                onDismiss = { vm.dismissStoryDialogue() }
            )
        }

        // Диалог офлайн-дохода при возвращении в игру
        if (offlineEarnings != null) {
            val earnings = offlineEarnings!!
            val hours = earnings.offlineSeconds / 3600
            val mins = (earnings.offlineSeconds % 3600) / 60

            Dialog(onDismissRequest = { vm.dismissOfflineDialog() }) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCyberCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Добро пожаловать обратно!",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Фермы работали без вас: ${hours}ч ${mins}мин",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        earnings.minedAmounts.forEach { (coinId, amount) ->
                            if (amount > 0.000001) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(DarkCyberCardElevated, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(coinId, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                    Text("+%.6f".format(amount), color = NeonGreen, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Счёт за свет:", color = TextSecondary, fontSize = 12.sp)
                            Text("-$%.2f USD".format(earnings.electricityCostUsd), color = NeonOrange, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { vm.dismissOfflineDialog() },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Забрать доход", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
