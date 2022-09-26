import React, { PropsWithChildren } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { UserRole } from "../services/userApi";
import { Guard } from "./Guard";

export interface Props extends PropsWithChildren {
  roles: UserRole | UserRole[];
  redirectUrl: string;
}

export const RouteGuard: React.FC<Props> = ({ roles, redirectUrl }) => {
  return <Guard element={<Outlet />} fallback={<Navigate to={redirectUrl} />} roles={roles} />;
};
