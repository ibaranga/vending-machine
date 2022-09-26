import React, { PropsWithChildren, useEffect, useState } from "react";
import { Box } from "@mui/material";

interface Props extends PropsWithChildren {
  updating: boolean;
}

export const HighlightAfterUpdate: React.FC<Props> = ({ children, updating }) => {
  const [opacity, setOpacity] = useState(1);

  useEffect(() => {
    if (!updating) {
      setOpacity(0.8);
      setTimeout(() => setOpacity(1), 300);
    }
  }, [updating]);

  return (
    <Box
      style={{
        backgroundColor: "secondary.main",
        opacity,
        transition: "opacity 100ms ease-in-out",
      }}
    >
      {children}
    </Box>
  );
};
