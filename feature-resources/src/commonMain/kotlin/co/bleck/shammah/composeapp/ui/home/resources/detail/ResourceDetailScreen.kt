package co.bleck.shammah.composeapp.ui.home.resources.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import co.bleck.shammah.composeapp.ui.components.ShimmerBox
import co.bleck.shammah.domain.model.Resource
import co.bleck.shammah.domain.model.ResourceType
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceDetailScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    onOpenUrl: (String) -> Unit
) {
    val resourceId = remember(backStackEntry) {
        backStackEntry.savedStateHandle.get<String>("resourceId").orEmpty()
    }
    val vm: ResourceDetailViewModel = koinViewModel(
        key = "resource_detail_$resourceId",
        parameters = { parametersOf(resourceId) }
    )
    val resource by vm.resource.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(resource?.title ?: "Recurso", maxLines = 2) },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        if (resource == null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ShimmerBox(height = 36.dp, cornerRadius = 8.dp)
                ShimmerBox(height = 180.dp, cornerRadius = 20.dp)
                ShimmerBox(height = 16.dp, cornerRadius = 6.dp)
                ShimmerBox(height = 16.dp, cornerRadius = 6.dp, modifier = Modifier.fillMaxWidth(0.8f))
            }
        } else {
            ResourceDetailContent(resource!!, innerPadding, onOpenUrl)
        }
    }
}

@Composable
private fun ResourceDetailContent(
    resource: Resource,
    innerPadding: androidx.compose.foundation.layout.PaddingValues,
    onOpenUrl: (String) -> Unit
) {
    var visible by remember(resource.id) { mutableStateOf(false) }
    LaunchedEffect(resource.id) { visible = true }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(450)) + slideInVertically(tween(450)) { 60 },
        modifier = Modifier.padding(innerPadding).fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(14.dp))
                }
                Column {
                    Text("Tipo de recurso", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(resource.type.label(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                }
            }
            Spacer(
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)).background(
                    Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.primary))
                )
            )
            Text(resource.description, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, lineHeight = MaterialTheme.typography.bodyLarge.lineHeight)
            resource.url?.takeIf { it.isNotBlank() }?.let { url ->
                Button(onClick = { onOpenUrl(url) }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.size(8.dp))
                    Text("Abrir recurso")
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

private fun ResourceType.label(): String = when (this) {
    ResourceType.reflection -> "Reflexión"
    ResourceType.study -> "Estudio"
    ResourceType.mission -> "Misión"
    ResourceType.vision -> "Visión"
    ResourceType.aboutus -> "Acerca de nosotros"
}
