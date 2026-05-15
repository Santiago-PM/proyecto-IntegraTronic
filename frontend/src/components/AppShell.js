import React from "react";
import { View, Text, Pressable, StyleSheet } from "react-native";
import { usePathname, router } from "expo-router";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import Sidebar from "./Sidebar";
import { colors, spacing } from "../constants/theme";
import { APP_NAME } from "../constants/app";
import { useAuth } from "../context/AuthContext";

const TITLE_BY_PATH = {
  "/dashboard": "Dashboard",
  "/usuarios": "Usuarios",
  "/productos": "Productos",
  "/familias": "Familias",
  "/consulta-stock": "Consulta Stock",
  "/entrada-mercancia": "Entrada Mercancía",
  "/venta-fisica": "Venta Física",
  "/clientes": "Clientes",
  "/pedido-online": "Pedido Online",
  "/informes": "Informes",
};

export default function AppShell({ children }) {
  const pathname = usePathname();
  const insets = useSafeAreaInsets();
  const { signOut } = useAuth();
  const headerTitle = TITLE_BY_PATH[pathname] || APP_NAME;

  return (
    <View style={[styles.root, { paddingTop: insets.top }]}>
      <View style={styles.row}>
        <Sidebar />
        <View style={styles.main}>
          <View style={styles.topBar}>
            <Text style={styles.topTitle}>{headerTitle}</Text>
            <Pressable
              onPress={() => {
                signOut();
                router.replace("/login");
              }}
              style={({ pressed }) => [styles.logoutBtn, pressed && { opacity: 0.75 }]}
              accessibilityLabel="Cerrar sesión"
            >
              <MaterialCommunityIcons name="logout" size={22} color={colors.white} />
            </Pressable>
          </View>
          <View style={styles.content}>{children}</View>
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  root: {
    flex: 1,
    backgroundColor: colors.bgPage,
  },
  row: {
    flex: 1,
    flexDirection: "row",
  },
  main: {
    flex: 1,
    minWidth: 0,
  },
  topBar: {
    height: 52,
    backgroundColor: colors.primary,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    paddingHorizontal: spacing.lg,
  },
  topTitle: {
    color: colors.white,
    fontSize: 17,
    fontWeight: "600",
  },
  logoutBtn: {
    padding: spacing.xs,
  },
  content: {
    flex: 1,
    minHeight: 0,
    padding: spacing.lg,
  },
});
