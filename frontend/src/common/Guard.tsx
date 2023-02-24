import React from "react";
import { UserRole } from "../services/usersApi";
import { useLoggedInUser } from "../hooks/useLoggedInUser";

interface GuardRule {
  roles: UserRole | UserRole[];
  element?: React.ReactElement;
}

interface Props {
  roles: UserRole | UserRole[];
  element?: React.ReactElement;
  fallback?: React.ReactElement;
}

export const Guard: React.FC<Props> = ({ roles, element, fallback }) => {
  const [user] = useLoggedInUser();
  const allowedRoles = Array.isArray(roles) ? roles : [roles];
  const isUserAllowed = !!user && allowedRoles.includes(user.role);
  return isUserAllowed ? element ?? null : fallback ?? null;
};
