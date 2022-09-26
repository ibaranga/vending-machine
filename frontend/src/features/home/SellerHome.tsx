import React from "react";
import { useLoggedInUser } from "../../hooks/useLoggedInUser";
import { Navigate } from "react-router-dom";
import { UserRole } from "../../services/userApi";
import { ProductGrid } from "../product/ProductGrid";

export const SellerHome: React.FC = () => {
  const [user] = useLoggedInUser();
  if (!user || user.role !== UserRole.SELLER) {
    return <Navigate to={"/"} />;
  }
  return (
    <>
      <ProductGrid />
    </>
  );
};
