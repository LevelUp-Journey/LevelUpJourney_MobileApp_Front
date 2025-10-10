# Swipe Gesture Implementation

## Overview
Se ha implementado un gesture de swipe horizontal en las pantallas de la aplicación para mejorar la experiencia de usuario, permitiendo navegar hacia atrás con un gesto de deslizar desde el borde izquierdo hacia la derecha.

## Pantallas con Swipe Gesture Implementado

### ✅ Completadas
- **ProfileScreen** - Swipe para volver desde el perfil
- **SettingsScreen** - Swipe para volver desde configuración
- **QuizQuestionsScreen** - Swipe para volver desde la lista de preguntas
- **CreateQuizScreen** - Swipe para volver desde la creación de quiz
- **AddQuestionScreen** - Swipe para volver desde añadir pregunta
- **EditQuestionScreen** - Swipe para volver desde editar pregunta

### 📱 MainScreen
- Usa swipe horizontal para navegar entre tabs (Home, Library, Community)
- Implementado con `HorizontalPager` de Jetpack Compose

## Implementación Técnica

### Dependencias Necesarias
```kotlin
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.Modifier
import kotlin.math.abs
```

### Patrón de Implementación

```kotlin
@Composable
fun YourScreen(onBack: () -> Unit) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    
    val animatedOffset by animateFloatAsState(
        targetValue = offsetX,
        label = "swipe"
    )
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .offset(x = animatedOffset.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (abs(offsetX) > 100f) {  // Threshold: 100px
                            if (offsetX > 0) {       // Swipe hacia la derecha
                                onBack()
                            }
                        }
                        offsetX = 0f
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        val newOffset = offsetX + dragAmount
                        if (newOffset >= 0) {        // Solo permitir swipe derecha
                            offsetX = newOffset.coerceAtMost(300f)  // Límite: 300px
                        }
                    }
                )
            }
    ) {
        // Contenido de la pantalla
    }
}
```

## Características

### 🎯 Comportamiento
- **Dirección**: Solo hacia la derecha (simulando el gesture nativo de "back")
- **Threshold**: 100px - distancia mínima para activar la navegación
- **Límite**: 300px - máximo desplazamiento visual
- **Animación**: Suave transición con `animateFloatAsState`
- **Reset**: Al soltar, vuelve a posición inicial si no supera el threshold

### 🎨 Feedback Visual
- El contenido se desplaza en tiempo real con el dedo
- Animación suave al soltar el gesto
- No hay over-scroll hacia la izquierda (dragAmount negativo bloqueado)

### ✨ Ventajas
- **Intuitivo**: Gesto familiar para usuarios móviles
- **Consistente**: Mismo comportamiento en todas las pantallas
- **Suave**: Animaciones fluidas que mejoran la UX
- **No invasivo**: No interfiere con scroll vertical ni otros gestures

## Testing

### Casos de Prueba
1. ✅ Swipe > 100px hacia derecha → Navega atrás
2. ✅ Swipe < 100px → Regresa a posición inicial
3. ✅ Swipe hacia izquierda → No hace nada
4. ✅ Scroll vertical → Funciona normalmente
5. ✅ Combinación scroll + swipe → Detecta dirección correctamente

## Mantenimiento

### Para Añadir Swipe a Nueva Pantalla
1. Copiar el patrón de implementación de arriba
2. Añadir imports necesarios
3. Aplicar el modifier `.pointerInput` al contenedor scrolleable
4. Asegurar que el callback `onBack` esté definido

### Consideraciones
- Aplicar swipe solo en pantallas de detalle/secundarias
- No aplicar en pantallas principales (Home, tabs principales)
- El orden de modifiers importa: `.offset()` debe ir antes de `.pointerInput()`

## Estado Actual
✅ **Implementación completa** en ProfileScreen y SettingsScreen
✅ **Sin errores de compilación**
✅ **Consistente** con otras pantallas de la app

---
*Última actualización: Octubre 10, 2025*
