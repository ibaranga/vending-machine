import React from "react";
import { Guard } from "../../common/Guard";
import { UserRole } from "../../services/usersApi";
import { Navigate } from "react-router-dom";

export const Home: React.FC = () => {
  return (
    <>
      <Guard roles={[UserRole.BUYER]} element={<Navigate to={"/buyer"} />} />
      <Guard roles={[UserRole.SELLER]} element={<Navigate to={"/seller"} />} />
      <Guard roles={[UserRole.BUYER, UserRole.SELLER]} fallback={<Navigate to={"/login"} />} />
    </>
  );
};
