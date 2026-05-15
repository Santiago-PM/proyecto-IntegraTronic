# IntegraTonic — front-end: cómo está montado y qué versiones usa

Este documento explica cómo se configuró el front-end (web y preparado para móvil), cómo ejecutarlo y las versiones principales de las herramientas. Los valores coinciden con el **`package.json`** en el momento de escribir la guía; tras actualizar dependencias, comprueba lo instalado con `npm list --depth=0`.

---

## Qué se construyó

- **Marco:** [Expo](https://expo.dev/) SDK **52** con **JavaScript** (solo archivos `.js`, sin TypeScript en el código de la app).
- **Capa de UI:** componentes de [React Native](https://reactnative.dev/) (`View`, `Text`, `TextInput`, etc.) para poder apuntar a **web** (mediante [React Native Web](https://necolas.github.io/react-native-web/)) y, si más adelante compilas nativo, a **iOS/Android**.
- **Rutas:** [Expo Router](https://docs.expo.dev/router/introduction/) (rutas basadas en archivos dentro de `app/`).
- **Layout:** shell compartido (`AppShell` + `Sidebar`) para las rutas autenticadas en `app/(main)/`; pantalla de login en `app/login.js`.
- **Autenticación:** solo simulada (`src/context/AuthContext.js`) — aún no hay API real; pensado para sustituir por el backend Spring Boot cuando exista.
- **Iconos:** `@expo/vector-icons` (MaterialCommunityIcons).

No hay pantallas hechas a mano en **HTML** ni **CSS** sueltos: el diseño y los estilos van en **JS** con `StyleSheet.create`. En web, Metro + React Native Web generan HTML/CSS en **`dist/`** cuando haces una **exportación estática** (más abajo).

---

## Estructura del repositorio (código que editas)

| Ruta | Función |
|------|---------|
| `app/` | Rutas y pantallas (Expo Router). |
| `app/_layout.js` | Layout raíz: safe area, proveedor de auth, stack, `<title>` en web con `expo-router/head`. |
| `app/(main)/` | Zona con sesión: shell + un archivo por pantalla principal. |
| `src/components/` | UI reutilizable (`AppShell`, `Sidebar`, primitivos en `ui.js`). |
| `src/constants/` | Colores del tema, espaciados, nombre de la app. |
| `src/context/` | Contexto de autenticación (mock). |

**No uses `dist/` como código fuente:** es salida de compilación.

---

## Cómo ejecutarlo en local

1. Instala **Node.js** (véase [Node.js](#nodejs) más abajo).
2. En la carpeta del proyecto:

   ```bash
   npm install
   ```

3. Arranca el servidor de desarrollo **web**:

   ```bash
   npm run web
   ```

   - Usa `expo start --web --offline` para que Expo no dependa de llamar a sus servidores (útil con firewalls estrictos).
   - Abre la URL que salga en la terminal (a menudo `http://localhost:8081`). Si el puerto está ocupado, Expo puede proponer otro (p. ej. **8082**).

Otros scripts:

- `npm start` — servidor de desarrollo de Expo; pulsa **`w`** para abrir web.
- `npm run web:online` — igual que web pero sin `--offline`.

---

## Exportación estática (`dist/`)

Para generar una carpeta que puedas subir a un hosting:

```bash
npm run export:web
```

Eso crea o actualiza **`dist/`** (SPA con `index.html` + bundles en `_expo/static/`). Esos archivos son **generados**; nombres de clase como `.r-…` vienen de **React Native Web** y son normales.

En desarrollo, el proyecto usa **`output: "single"`** en `app.json` para evitar errores de renderizado en servidor (pantalla en blanco / avisos de `useLayoutEffect`).

---

## Versiones y herramientas

### React

| Paquete | Versión en `package.json` |
|---------|---------------------------|
| `react` | **18.3.1** |
| `react-dom` | **18.3.1** (web) |
| `react-native` | **0.76.3** |

### Expo y router

| Paquete | Versión |
|---------|---------|
| `expo` | **~52.0.0** |
| `expo-router` | **~4.0.0** |
| `expo-constants` | **~17.0.3** |
| `expo-linking` | **~7.0.3** |
| `expo-status-bar` | **~2.0.0** |

### React Native Web (renderizado en navegador)

| Paquete | Versión |
|---------|---------|
| `react-native-web` | **~0.19.13** |

### Otras librerías en runtime

| Paquete | Versión |
|---------|---------|
| `@expo/vector-icons` | **^14.0.4** |
| `@react-navigation/native` | **^7.0.14** |
| `react-native-safe-area-context` | **4.12.0** |
| `react-native-screens` | **~4.4.0** |

### Compilación / transpilado (desarrollo)

| Paquete | Versión |
|---------|---------|
| `@babel/core` | **^7.25.2** |
| `babel-preset-expo` | **~12.0.0** |

### JavaScript (“versión de JS”)

En `package.json` **no** figura un año concreto de ECMAScript. Los ficheros son **`.js`**; **Babel** (`babel-preset-expo`) los transpila para Metro y los objetivos que soporte Expo. Usa sintaxis JS moderna compatible con Expo 52.

### HTML / CSS

- **HTML:** lo genera la exportación de Expo y la tubería de RN Web; no se mantienen a mano grandes `.html` por pantalla.
- **CSS:** sale de la compilación de **React Native Web** a partir de `StyleSheet` (y similares). Tú defines estilos en JS; el navegador recibe CSS generado (incluidas clases cortas en `dist/`).

### Node.js {#nodejs}

En este repositorio **no** está fijada una versión de Node en el campo `engines` de `package.json`. Instala un **Node LTS** actual (p. ej. **20.x** o **22.x**) compatible con [Expo SDK 52](https://docs.expo.dev/).

En tu máquina:

```bash
node -v
npm -v
```

**npm** viene con Node; el archivo de bloqueo es **`package-lock.json`** (lo genera `npm install`).

---

## Backend (Spring Boot) — aún no conectado

La app **no** llama aún a tu API. Cuando el backend esté listo, lo habitual será: URL base configurable, CORS en Spring Boot, sustituir el login mock (`signIn` / sesión) por uno real (p. ej. JWT) y usar `fetch` o axios desde las pantallas.

---

## Resumen

| Pieza | Qué es |
|-------|--------|
| **React** | 18.3.1 |
| **React Native** | 0.76.3 |
| **Expo** | SDK ~52 |
| **Router** | Expo Router ~4 |
| **Web** | React Native Web ~0.19 |
| **Lenguaje** | JavaScript (`.js`) |
| **HTML/CSS** | Generados en web a partir de RN + export; se edita `app/` y `src/` |
| **Node** | Se recomienda LTS; no fijado en el repo |
| **Gestor de paquetes** | npm (`package-lock.json`) |

---

*Última revisión alineada con el `package.json` del repo front-end IntegraTonic.*
