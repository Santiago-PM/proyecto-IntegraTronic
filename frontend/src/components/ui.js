import React from "react";
import { View, Text, TextInput, Pressable, StyleSheet, Platform } from "react-native";
import { colors, radius, spacing } from "../constants/theme";

export function Card({ children, style }) {
  return <View style={[styles.card, style]}>{children}</View>;
}

export function PageHeader({ title, subtitle, right }) {
  return (
    <View style={phStyles.row}>
      <View style={{ flex: 1 }}>
        <Text style={phStyles.title}>{title}</Text>
        {subtitle ? <Text style={phStyles.sub}>{subtitle}</Text> : null}
      </View>
      {right}
    </View>
  );
}

const phStyles = StyleSheet.create({
  row: {
    flexDirection: "row",
    alignItems: "flex-start",
    justifyContent: "space-between",
    marginBottom: spacing.lg,
    gap: spacing.md,
  },
  title: {
    fontSize: 24,
    fontWeight: "700",
    color: colors.text,
  },
  sub: {
    marginTop: 4,
    fontSize: 14,
    color: colors.textMuted,
  },
});

export function PrimaryButton({ title, onPress, icon, disabled, style }) {
  return (
    <Pressable
      onPress={onPress}
      disabled={disabled}
      style={({ pressed }) => [
        styles.primaryBtn,
        disabled && styles.primaryBtnDisabled,
        pressed && !disabled && { opacity: 0.9 },
        style,
      ]}
    >
      {icon}
      <Text style={[styles.primaryBtnText, disabled && { color: colors.textMuted }]}>{title}</Text>
    </Pressable>
  );
}

export function OutlineButton({ title, onPress, icon, style }) {
  return (
    <Pressable
      onPress={onPress}
      style={({ pressed }) => [styles.outlineBtn, pressed && { opacity: 0.85 }, style]}
    >
      {icon}
      <Text style={styles.outlineBtnText}>{title}</Text>
    </Pressable>
  );
}

export function Badge({ text, color = colors.success }) {
  return (
    <View style={[styles.badge, { backgroundColor: color }]}>
      <Text style={styles.badgeText}>{text}</Text>
    </View>
  );
}

export function SearchField({ placeholder, value, onChangeText, rightSlot }) {
  return (
    <View style={styles.searchRow}>
      <View style={styles.searchInputWrap}>
        <Text style={styles.searchIcon}>🔍</Text>
        <TextInput
          style={styles.searchInput}
          placeholder={placeholder}
          placeholderTextColor={colors.textLight}
          value={value}
          onChangeText={onChangeText}
        />
      </View>
      {rightSlot}
    </View>
  );
}

export function TableShell({ columns, rows, emptyText, loading, error, footer }) {
  const hasRows = Array.isArray(rows) && rows.length > 0;

  return (
    <Card style={{ padding: 0, overflow: "hidden" }}>
      <View style={styles.tableHead}>
        {columns.map((c) => (
          <Text key={c} style={styles.th}>
            {c}
          </Text>
        ))}
      </View>
      <View style={styles.tableDivider} />
      {loading ? (
        <View style={styles.emptyCell}>
          <Text style={styles.emptyText}>Cargando...</Text>
        </View>
      ) : error ? (
        <View style={styles.emptyCell}>
          <Text style={[styles.emptyText, { color: colors.danger }]}>{error}</Text>
        </View>
      ) : hasRows ? (
        rows.map((row, idx) => (
          <View key={row.key ?? idx}>
            <View style={styles.tableRow}>
              {row.cells.map((cell, cellIdx) =>
                React.isValidElement(cell) ? (
                  <View key={`${idx}-${cellIdx}`} style={styles.tdWrap}>
                    {cell}
                  </View>
                ) : (
                  <Text key={`${idx}-${cellIdx}`} style={styles.td} numberOfLines={2}>
                    {cell}
                  </Text>
                )
              )}
            </View>
            <View style={styles.tableDivider} />
          </View>
        ))
      ) : (
        <View style={styles.emptyCell}>
          <Text style={styles.emptyText}>{emptyText}</Text>
        </View>
      )}
      {footer}
    </Card>
  );
}

export function ActionLink({ label = "Borrar", onPress, color = colors.danger }) {
  return (
    <Pressable onPress={onPress}>
      <Text style={{ color, fontSize: 13, fontWeight: "600" }}>{label}</Text>
    </Pressable>
  );
}

export function PaginationFooter({ total = 0 }) {
  const shown = total > 0 ? `1 - ${total}` : "0 - 0";
  return (
    <View style={styles.pgFooter}>
      <Text style={styles.pgMeta}>Filas por página: 10</Text>
      <Text style={styles.pgMeta}>
        {shown} of {total}
      </Text>
      <Text style={styles.pgMeta}>‹ ›</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: colors.white,
    borderRadius: radius.md,
    padding: spacing.lg,
    borderWidth: Platform.OS === "web" ? 1 : StyleSheet.hairlineWidth,
    borderColor: colors.border,
    ...Platform.select({
      web: {
        boxShadow: "0 1px 3px rgba(0,0,0,0.06)",
      },
      default: {
        elevation: 2,
      },
    }),
  },
  primaryBtn: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 8,
    backgroundColor: colors.primary,
    paddingVertical: 12,
    paddingHorizontal: 16,
    borderRadius: radius.md,
  },
  primaryBtnDisabled: {
    backgroundColor: "#E5E7EB",
  },
  primaryBtnText: {
    color: colors.white,
    fontWeight: "700",
    fontSize: 15,
  },
  outlineBtn: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 8,
    borderWidth: 1,
    borderColor: colors.primary,
    paddingVertical: 10,
    paddingHorizontal: 14,
    borderRadius: radius.md,
    backgroundColor: colors.white,
  },
  outlineBtnText: {
    color: colors.primary,
    fontWeight: "600",
    fontSize: 14,
  },
  badge: {
    alignSelf: "flex-start",
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 999,
  },
  badgeText: {
    color: colors.white,
    fontSize: 12,
    fontWeight: "600",
  },
  searchRow: {
    flexDirection: "row",
    alignItems: "center",
    gap: spacing.sm,
    marginBottom: spacing.md,
  },
  searchInputWrap: {
    flex: 1,
    flexDirection: "row",
    alignItems: "center",
    borderWidth: 1,
    borderColor: colors.border,
    borderRadius: radius.md,
    paddingHorizontal: spacing.md,
    backgroundColor: colors.white,
    minHeight: 44,
  },
  searchIcon: {
    fontSize: 16,
    marginRight: 8,
  },
  searchInput: {
    flex: 1,
    fontSize: 15,
    color: colors.text,
    paddingVertical: Platform.OS === "web" ? 10 : 8,
  },
  tableHead: {
    flexDirection: "row",
    paddingHorizontal: spacing.md,
    paddingVertical: spacing.sm,
    flexWrap: "wrap",
  },
  th: {
    flex: 1,
    minWidth: 72,
    fontSize: 11,
    fontWeight: "700",
    color: colors.textMuted,
    textTransform: "uppercase",
  },
  tableDivider: {
    height: 1,
    backgroundColor: colors.border,
  },
  emptyCell: {
    minHeight: 160,
    alignItems: "center",
    justifyContent: "center",
    padding: spacing.lg,
  },
  emptyText: {
    color: colors.textMuted,
    fontSize: 15,
  },
  tableRow: {
    flexDirection: "row",
    paddingHorizontal: spacing.md,
    paddingVertical: spacing.sm,
    flexWrap: "wrap",
  },
  td: {
    flex: 1,
    minWidth: 72,
    fontSize: 13,
    color: colors.text,
  },
  tdWrap: {
    flex: 1,
    minWidth: 72,
    justifyContent: "center",
  },
  pgFooter: {
    flexDirection: "row",
    justifyContent: "flex-end",
    alignItems: "center",
    gap: spacing.lg,
    padding: spacing.md,
    borderTopWidth: 1,
    borderTopColor: colors.border,
  },
  pgMeta: {
    fontSize: 13,
    color: colors.textMuted,
  },
});

export function MetricCard({ title, value, sub, subColor = colors.success, iconBg, icon }) {
  return (
    <Card style={mcStyles.card}>
      <View style={mcStyles.head}>
        <View>
          <Text style={mcStyles.title}>{title}</Text>
          <Text style={mcStyles.value}>{value}</Text>
          {sub ? (
            <Text style={[mcStyles.sub, { color: subColor }]}>
              {sub}
            </Text>
          ) : null}
        </View>
        <View style={[mcStyles.iconBox, { backgroundColor: iconBg }]}>{icon}</View>
      </View>
    </Card>
  );
}

const mcStyles = StyleSheet.create({
  card: {
    flex: 1,
    minWidth: 140,
    padding: spacing.md,
  },
  head: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "flex-start",
  },
  title: {
    fontSize: 13,
    color: colors.textMuted,
    marginBottom: 6,
  },
  value: {
    fontSize: 26,
    fontWeight: "800",
    color: colors.text,
  },
  sub: {
    marginTop: 4,
    fontSize: 13,
    fontWeight: "600",
  },
  iconBox: {
    width: 40,
    height: 40,
    borderRadius: radius.sm,
    alignItems: "center",
    justifyContent: "center",
  },
});

export function StatMini({ label, value, valueColor, iconBg, icon }) {
  return (
    <Card style={smStyles.card}>
      <View style={smStyles.top}>
        <Text style={smStyles.label}>{label}</Text>
        {icon ? <View style={[smStyles.iconBox, { backgroundColor: iconBg || colors.primary }]}>{icon}</View> : null}
      </View>
      <Text style={[smStyles.value, valueColor && { color: valueColor }]}>{value}</Text>
    </Card>
  );
}

const smStyles = StyleSheet.create({
  card: {
    flex: 1,
    minWidth: 120,
    padding: spacing.md,
  },
  top: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 8,
  },
  label: {
    fontSize: 13,
    color: colors.textMuted,
    flex: 1,
  },
  iconBox: {
    width: 36,
    height: 36,
    borderRadius: radius.sm,
    alignItems: "center",
    justifyContent: "center",
  },
  value: {
    fontSize: 22,
    fontWeight: "700",
    color: colors.text,
  },
});
