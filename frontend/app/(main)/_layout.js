import { Slot, Redirect } from "expo-router";
import { useAuth } from "../../src/context/AuthContext";
import AppShell from "../../src/components/AppShell";

export default function MainLayout() {  const { user } = useAuth();
  if (!user) return <Redirect href="/login" />;
  return (
    <AppShell>
      <Slot />
    </AppShell>
  );
}
