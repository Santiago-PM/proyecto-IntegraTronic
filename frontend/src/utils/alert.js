import { Alert, Platform } from "react-native";

export function showAlert(title, message) {
  if (Platform.OS === "web") {
    window.alert(message ? `${title}\n\n${message}` : title);
  } else {
    Alert.alert(title, message);
  }
}

export function confirmAction(title, message) {
  return new Promise((resolve) => {
    if (Platform.OS === "web") {
      resolve(window.confirm(message ? `${title}\n\n${message}` : title));
    } else {
      Alert.alert(title, message, [
        { text: "Cancelar", style: "cancel", onPress: () => resolve(false) },
        { text: "Confirmar", style: "destructive", onPress: () => resolve(true) },
      ]);
    }
  });
}
