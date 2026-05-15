import React, { Fragment } from "react";
import { Stack } from "expo-router";
import Head from "expo-router/head";
import { SafeAreaProvider } from "react-native-safe-area-context";
import { AuthProvider } from "../src/context/AuthContext";
import { APP_NAME } from "../src/constants/app";

export default function RootLayout() {
  return (
    <Fragment>
      <Head>
        <title>{APP_NAME}</title>
        <meta name="description" content={`${APP_NAME} — ERP`} />
      </Head>
      <SafeAreaProvider>
        <AuthProvider>
          <Stack screenOptions={{ headerShown: false }} />
        </AuthProvider>
      </SafeAreaProvider>
    </Fragment>
  );
}
