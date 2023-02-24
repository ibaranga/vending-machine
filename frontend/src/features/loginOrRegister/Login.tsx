import React, { useEffect } from "react";
import { Controller, useForm } from "react-hook-form";
import { LoginRequest, useLoginMutation, UserTokenClaims } from "../../services/usersApi";
import {
  Alert,
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  TextField,
} from "@mui/material";
import { useLoggedInUser } from "../../hooks/useLoggedInUser";
import { useJwt } from "react-jwt";
import { Navigate } from "react-router-dom";

export const Login: React.FC = () => {
  const {
    control,
    handleSubmit,
    formState: { errors, isValid, isDirty, isValidating },
  } = useForm<LoginRequest>({
    defaultValues: {
      username: "",
      password: "",
    },
    reValidateMode: "onChange",
    mode: "onChange",
    delayError: 1000,
  });
  const [login, loginResult] = useLoginMutation();
  const jwt = useJwt<UserTokenClaims>(loginResult?.data?.accessToken ?? "");
  const [user, setUser] = useLoggedInUser();

  useEffect(() => {
    if (loginResult.data && jwt.decodedToken) {
      setUser({
        ...loginResult.data,
        role: jwt.decodedToken.role,
      });
    }
  }, [loginResult.data && jwt.decodedToken]);

  if (user) {
    return <Navigate to={"/"} />;
  }

  return (
    <form onSubmit={handleSubmit(login)}>
      <Card sx={{ minWidth: "350px", height: "100%", display: "flex", flexDirection: "column" }}>
        <CardHeader title={" "} />
        <CardContent sx={{ display: "flex", flexDirection: "column", gap: "10px" }}>
          <Controller
            name="username"
            control={control}
            rules={{ minLength: 4, maxLength: 64, required: true }}
            render={({ field }) => (
              <TextField
                id={"login-username"}
                error={!!errors.username}
                helperText={errors.username ? "4-64 characters" : " "}
                autoComplete={"username"}
                label={"Username"}
                variant={"standard"}
                {...field}
              />
            )}
          />

          <Controller
            name="password"
            control={control}
            rules={{ minLength: 8, maxLength: 32, required: true }}
            render={({ field }) => (
              <TextField
                id={"login-password"}
                error={!!errors.password}
                helperText={errors.password ? "8-32 characters" : " "}
                autoComplete={"password"}
                label={"Password"}
                type={"password"}
                variant={"standard"}
                {...field}
              />
            )}
          />
          <Box sx={{ minHeight: 50 }}>
            {loginResult?.isError && <Alert severity={"error"}>Login failed</Alert>}
          </Box>
        </CardContent>

        <CardActions sx={{ width: "100%" }}>
          <Button
            id={"login-submit"}
            disabled={loginResult.isLoading || !isValid}
            variant={"contained"}
            type="submit"
          >
            Login
          </Button>
        </CardActions>
      </Card>
    </form>
  );
};
