import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function ({ children }) {

    const { isUserAuthenticated } = useAuth();

    const navigate = useNavigate();
     
    useEffect(() => {
        if (!isUserAuthenticated()) {
            navigate("/")
        }

    });

    return isUserAuthenticated() ? children: "";
    
}