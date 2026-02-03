package com.example.triptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.triptracker.ui.theme.LocalTripGradient
import com.example.triptracker.ui.theme.TripTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripTrackerTheme {
                TripTrackerApp()
            }
        }
    }
}

@Composable
fun TripTrackerApp() {
    val navController = rememberNavController()
    val items = remember {
        listOf(
            TopLevelDestination("home", "Overview", Icons.Filled.Home),
            TopLevelDestination("rewards", "Rewards", Icons.Filled.CardGiftcard),
            TopLevelDestination("insights", "Insights", Icons.Filled.Explore)
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { destination ->
                    val selected = currentDestination?.route == destination.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") {
                OverviewScreen()
            }
            composable("rewards") {
                RewardsScreen()
            }
            composable("insights") {
                InsightsScreen()
            }
        }
    }
}

@Composable
fun OverviewScreen() {
    val gradient = LocalTripGradient.current
    val trips = remember {
        listOf(
            TripPlan("Coastal Escape", "May 18 - May 24", "Lake Como, Positano", 4.8),
            TripPlan("City Lights", "Jun 2 - Jun 8", "Tokyo, Kyoto", 4.6),
            TripPlan("Nordic Trails", "Jul 10 - Jul 19", "Reykjavik, Vik", 4.9)
        )
    }
    val dateChoices = remember {
        listOf("May 18", "May 19", "May 20", "May 21", "May 22", "May 23")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient.background),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Plan Your Trip",
                text = "Upcoming Adventures",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Select dates, review trip details, and refine the route with AI insights.",
                text = "Plan, review, and refine your itinerary with smart guidance.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Trip Date Planner",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Choose your travel window",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(dateChoices) { date ->
                            DateChip(text = date)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Selected: May 18 - May 24 · 6 nights",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        item {
            Text(
                text = "Trip Details",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(trips) { trip ->
            TripCard(trip)
        }

        item {
            AiInsightCard(
                title = "AI Hyper-Analysis",
                insight = "Most visited: Lake Como (4.9★). Lakefront routes draw the highest ratings and longer stay times.",
                nextStep = "Boost your score with sunrise docks and a local tasting tour."
                insight = "Most visited: Lake Como (4.9★). Travelers spend 32% more time by the lakefront with sunset ferry routes.",
                nextStep = "Boost your rating by booking sunrise docks + local tasting tour."
            )
        }
    }
}

@Composable
fun DateChip(text: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun TripCard(trip: TripPlan) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = trip.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${trip.rating}★",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = trip.dates,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = trip.highlights,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AiInsightCard(title: String, insight: String, nextStep: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.92f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = insight,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = nextStep,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
            )
        }
    }
}

@Composable
fun RewardsScreen() {
    val gradient = LocalTripGradient.current
    val rewards = remember {
        listOf(
            RewardItem("Referral Bonus", "Invite friends and earn 300 points per signup", "Active"),
            RewardItem("Premium Lounge", "Unlock airport lounge passes at 2,000 points", "In progress"),
            RewardItem("Hotel Upgrade", "Free room upgrade after 5 completed stays", "Eligible")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient.background),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Rewards & Referrals",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "You have 1,250 points. Share your code to unlock travel perks.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.92f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "1,250 points",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Gold tier",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { 0.62f },
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "750 points to unlock Platinum lounge access.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Referral Code",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "TRIP-4829",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Every new traveler earns you 300 points + priority boarding upgrade.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        items(rewards) { reward ->
            RewardCard(reward)
        }
    }
}

@Composable
fun RewardCard(reward: RewardItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = reward.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = reward.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = reward.status,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun InsightsScreen() {
    val gradient = LocalTripGradient.current
    val updates = remember {
        listOf(
            TravelInsight("Rome", "Cultural festival week", "Sunny 27°C"),
            TravelInsight("Kyoto", "Temple night tour opening", "Clear 22°C"),
            TravelInsight("Reykjavik", "Midnight sun hikes", "Breezy 14°C")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient.background),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Live Trip Insights",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Track local affairs and climate status for each destination.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Today’s Snapshot",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SnapshotCard(title = "Current affairs", value = "Festival week")
                        SnapshotCard(title = "Climate status", value = "27°C · Sunny")
                    }
                }
            }
        }

        items(updates) { insight ->
            InsightCard(insight)
        }
    }
}

@Composable
fun SnapshotCard(title: String, value: String) {
    Card(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun InsightCard(insight: TravelInsight) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = insight.city,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = insight.currentAffair,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = insight.climate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Immutable
data class TripPlan(
    val name: String,
    val dates: String,
    val highlights: String,
    val rating: Double
)

@Immutable
data class RewardItem(
    val title: String,
    val description: String,
    val status: String
)

@Immutable
data class TravelInsight(
    val city: String,
    val currentAffair: String,
    val climate: String
)

@Immutable
data class TopLevelDestination(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Preview(showBackground = true)
@Composable
fun OverviewScreenPreview() {
    TripTrackerTheme {
        OverviewScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun RewardsScreenPreview() {
    TripTrackerTheme {
        RewardsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun InsightsScreenPreview() {
    TripTrackerTheme {
        InsightsScreen()
    }
}
