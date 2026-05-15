import Constants from "expo-constants";
import { Platform } from "react-native";

const DEFAULT_PORT = 8080;

function resolveDevHost() {
  const hostUri = Constants.expoConfig?.hostUri;
  if (hostUri) {
    const host = hostUri.split(":")[0];
    if (host && host !== "localhost") return host;
  }
  if (Platform.OS === "android") return "10.0.2.2";
  return "localhost";
}

export function getApiBaseUrl() {
  const configured = Constants.expoConfig?.extra?.apiUrl;
  if (configured) return configured.replace(/\/$/, "");
  return `http://${resolveDevHost()}:${DEFAULT_PORT}`;
}
