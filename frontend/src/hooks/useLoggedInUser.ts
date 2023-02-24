import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../store";
import {
  LoginState,
  removeLoggedInUser,
  setLoggedInUser,
} from "../features/loginOrRegister/loginSlice";
import { User } from "../services/usersApi";

export function useLoggedInUser(): [User | null, (user: User | null) => void] {
  const dispatch = useDispatch();
  const userState = useSelector<RootState, LoginState>((state) => state.login);
  return [
    userState.user ?? null,
    (user) => dispatch(user ? setLoggedInUser(user) : removeLoggedInUser()),
  ];
}
