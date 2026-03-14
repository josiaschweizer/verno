import { createBrowserRouter } from "react-router-dom";
import RootLayout from "./components/layouts/RootLayout.tsx";

import Home from "@/routes/page";
import EntryPage from "@/routes/entry/page.tsx";
import PaymentPage from "@/routes/payment/page";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <RootLayout />,
    children: [
      { index: true, element: <Home /> },

      { path: "entry", element: <EntryPage /> },
      { path: "payment", element: <PaymentPage /> },
    ],
  },
]);