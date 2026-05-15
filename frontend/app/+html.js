import { ScrollViewStyleReset } from "expo-router/html";
import { APP_NAME } from "../src/constants/app";

// Plantilla HTML para web (título y viewport correctos; evita página vacía en el primer render)
export default function Root({ children }) {
  return (
    <html lang="es">
      <head>
        <meta charSet="utf-8" />
        <meta httpEquiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <title>{APP_NAME}</title>
        <meta name="description" content={`${APP_NAME} — ERP`} />
        <ScrollViewStyleReset />
      </head>
      <body>{children}</body>
    </html>
  );
}
