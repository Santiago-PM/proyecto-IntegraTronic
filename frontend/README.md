# IntegraTonic — Frontend (Expo / React Native Web)

App ERP con Expo Router. Se comunica con el backend Spring Boot en `http://localhost:8080`.

## Requisitos

- Node.js LTS (20+)
- Backend en marcha (ver `../iniciar-backend.ps1` o Docker en la raíz del repo)

## Arrancar en desarrollo

```bash
cd frontend
npm install
npm run web
```

Abre la URL que indique Expo (p. ej. `http://localhost:8081`).

## Login de prueba (datos demo con Docker)

- Usuario: `demo@integratronic.com`
- Contraseña: `demo123`

## Scripts

| Comando | Descripción |
|---------|-------------|
| `npm run web` | Desarrollo web |
| `npm run export:web` | Build estático en `dist/` |

La URL del API se configura en `app.json` → `extra.apiUrl`.
