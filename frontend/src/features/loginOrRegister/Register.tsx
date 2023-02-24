import React from "react";
import { Controller, useForm } from "react-hook-form";
import { CreateUserRequest, useCreateUserMutation, UserRole } from "../../services/usersApi";
import {
  Alert,
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import { Navigate } from "react-router-dom";

export const Register: React.FC = () => {
  const {
    control,
    handleSubmit,
    formState: { errors, isValid },
    getValues,
  } = useForm<CreateUserRequest & { passwordCheck: string }>({
    defaultValues: {
      role: UserRole.BUYER,
      username: "",
      password: "",
      passwordCheck: "",
    },
    reValidateMode: "onChange",
    mode: "onChange",
    delayError: 1000,
  });
  const [createUser, createUserResult] = useCreateUserMutation();

  if (createUserResult.isSuccess) {
    return <Navigate to={"/login"} />;
  }

  return (
    <form onSubmit={handleSubmit(createUser)}>
      <Card sx={{ minWidth: "350px", height: "100%", display: "flex", flexDirection: "column" }}>
        <CardHeader title={" "} />
        <CardContent sx={{ display: "flex", flexDirection: "column", gap: "10px" }}>
          <Controller
            name="username"
            control={control}
            rules={{ minLength: 4, maxLength: 64, required: true }}
            render={({ field }) => (
              <TextField
                id={"register-username"}
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
                id={"register-password"}
                error={!!errors.password}
                helperText={errors.password ? "8-32 characters" : " "}
                autoComplete={"new-password"}
                label={"Password"}
                type={"password"}
                variant={"standard"}
                {...field}
              />
            )}
          />

          <Controller
            name="passwordCheck"
            control={control}
            rules={{
              deps: "password",
              validate: (value: string) => value === getValues().password,
            }}
            render={({ field }) => (
              <TextField
                id={"register-confirm-password"}
                error={!!errors.passwordCheck}
                helperText={errors.passwordCheck ? "Passwords don't match" : " "}
                autoComplete={"new-password"}
                label={"Confirm Password"}
                type={"password"}
                variant={"standard"}
                {...field}
              />
            )}
          />
          <InputLabel>Role</InputLabel>
          <Controller
            name="role"
            control={control}
            render={({ field }) => (
              <Select
                id={"register-role"}
                {...field}
                size={"small"}
                variant={"standard"}
                label={"Role"}
              >
                <MenuItem id={"register-role-buyer"} value={UserRole.BUYER}>
                  Buyer
                </MenuItem>
                <MenuItem id={"register-role-seller"} value={UserRole.SELLER}>
                  Seller
                </MenuItem>
              </Select>
            )}
          />
          <Box sx={{ minHeight: 50 }}>
            {createUserResult.isError && <Alert severity={"error"}>Registration failed</Alert>}
          </Box>
        </CardContent>
        <CardActions>
          <Button
            id={"register-submit"}
            variant={"contained"}
            disabled={createUserResult.isLoading || !isValid}
            type="submit"
          >
            Register
          </Button>
        </CardActions>
      </Card>
    </form>
  );
};
