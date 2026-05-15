import React from "react";
import { View, Text, Pressable, StyleSheet, ScrollView } from "react-native";
import { usePathname, router } from "expo-router";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { colors, radius, spacing } from "../constants/theme";
import { APP_NAME } from "../constants/app";

const ITEMS = [
  { href: "/dashboard", label: "Dashboard", icon: "view-dashboard-outline" },
  { href: "/usuarios", label: "Usuarios", icon: "account-group-outline" },
  { href: "/productos", label: "Productos", icon: "package-variant" },
  { href: "/familias", label: "Familias", icon: "shape-outline" },
  { href: "/consulta-stock", label: "Consulta Stock", icon: "warehouse" },
  { href: "/entrada-mercancia", label: "Entrada Mercancía", icon: "truck-delivery-outline" },
  { href: "/venta-fisica", label: "Venta Física", icon: "cart-outline" },
  { href: "/clientes", label: "Clientes", icon: "account-outline" },
  { href: "/pedido-online", label: "Pedido Online", icon: "shopping-outline" },
  { href: "/informes", label: "Informes", icon: "file-chart-outline" },
];

export default function Sidebar() {
  const pathname = usePathname();

  return (
    <View style={styles.wrap}>
      <ScrollView contentContainerStyle={styles.scroll} showsVerticalScrollIndicator={false}>
        <View style={styles.brandBlock}>
          <Text style={styles.brandName}>{APP_NAME}</Text>
          <Text style={styles.brandHint}>ERP</Text>
        </View>
        {ITEMS.map((item) => {
          const active = pathname === item.href || pathname.startsWith(item.href + "/");
          return (
            <Pressable
              key={item.href}
              onPress={() => router.push(item.href)}
              style={({ pressed }) => [
                styles.item,
                active && styles.itemActive,
                pressed && styles.itemPressed,
              ]}
            >
              <MaterialCommunityIcons
                name={item.icon}
                size={20}
                color={active ? colors.primary : colors.textMuted}
              />
              <Text style={[styles.itemText, active && styles.itemTextActive]}>{item.label}</Text>
            </Pressable>
          );
        })}
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  wrap: {
    width: 220,
    backgroundColor: colors.bgSidebar,
    borderRightWidth: 1,
    borderRightColor: colors.border,
  },
  scroll: {
    paddingVertical: spacing.md,
    paddingHorizontal: spacing.sm,
  },
  brandBlock: {
    paddingHorizontal: spacing.md,
    paddingBottom: spacing.lg,
    marginBottom: spacing.sm,
    borderBottomWidth: 1,
    borderBottomColor: colors.border,
  },
  brandName: {
    fontSize: 18,
    fontWeight: "800",
    color: colors.primary,
    letterSpacing: -0.3,
  },
  brandHint: {
    marginTop: 2,
    fontSize: 11,
    fontWeight: "600",
    color: colors.textMuted,
    textTransform: "uppercase",
    letterSpacing: 0.5,
  },
  item: {
    flexDirection: "row",
    alignItems: "center",
    gap: spacing.sm,
    paddingVertical: spacing.sm + 2,
    paddingHorizontal: spacing.md,
    borderRadius: radius.md,
    marginBottom: 4,
  },
  itemActive: {
    backgroundColor: colors.activeNavBg,
  },
  itemPressed: {
    opacity: 0.85,
  },
  itemText: {
    fontSize: 14,
    color: colors.text,
  },
  itemTextActive: {
    color: colors.primary,
    fontWeight: "600",
  },
});
