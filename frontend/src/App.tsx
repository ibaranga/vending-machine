import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { RouteGuard } from "./common/RouteGuard";
import { UserRole } from "./services/usersApi";
import { Home } from "./features/home/Home";
import { BuyerHome } from "./features/home/BuyerHome";
import { SellerHome } from "./features/home/SellerHome";
import { NotFound } from "./features/notFound/NotFound";
import { Header } from "./common/Header";
import { Box, createTheme, CssBaseline, styled, ThemeProvider } from "@mui/material";
import { AppAlert } from "./features/alert/AppAlert";
import { LoginAlert } from "./features/loginOrRegister/LoginAlert";
import { LoginOrRegister, TabIndex } from "./features/loginOrRegister/LoginOrRegister";

const RootBox = styled(Box)(({ theme }) => ({
  display: "flex",
  justifyContent: "center",
  [theme.breakpoints.up("xs")]: {
    margin: "1em",
  },
  [theme.breakpoints.up("md")]: {
    margin: "2em",
  },
}));

function App() {
  const defaultTheme = createTheme({ palette: { mode: "dark" } });
  const theme = createTheme({
    palette: {
      mode: "dark",
      primary: defaultTheme.palette.secondary,
      secondary: defaultTheme.palette.primary,
    },
  });
  return (
    <React.Fragment>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <Header />
        <RootBox>
          <BrowserRouter>
            <Routes>
              <Route path={"/"} element={<Home />} />
              <Route element={<RouteGuard roles={UserRole.BUYER} redirectUrl={"/"} />}>
                <Route path={"/buyer"} element={<BuyerHome />} />
              </Route>
              <Route element={<RouteGuard roles={UserRole.SELLER} redirectUrl={"/"} />}>
                <Route path={"/seller"} element={<SellerHome />} />
              </Route>
              <Route path={"/login"} element={<LoginOrRegister index={TabIndex.LOGIN} />} />
              <Route path={"/register"} element={<LoginOrRegister index={TabIndex.REGISTER} />} />
              <Route path={"*"} element={<NotFound />} />
            </Routes>
            <AppAlert />
            <LoginAlert />
          </BrowserRouter>
        </RootBox>
      </ThemeProvider>
    </React.Fragment>
  );
}

export default App;
