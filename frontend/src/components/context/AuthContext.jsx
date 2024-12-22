import {
    Children,
    createContext,
    useContext,
    useEffect,
    useState
} from "react";
import { fetchTeacherData, login as performLogin } from "../../services/client.js";
import { jwtDecode } from "jwt-decode";

const AuthContext = createContext({});

const AuthProvider = ({ children }) => {

    const [person, setPerson] = useState(null);

    const setTeacherFromToken = () => {
        let token = localStorage.getItem("access_token");
        if (token) {
            token = jwtDecode(token)
            setPerson({
                username: token.sub,
                roles: token.Scopes
            })
        }
    }


    useEffect(() => {
        setTeacherFromToken();
        //      // Теперь выполняем запрос к API для получения данных о пользователе
        // fetchTeacherData().then(userData => {
        //     setPerson({
        //         avatarUrl: userData[1].avatarUrl, // Получаем avatarUrl из ответа
                
        //     });
        // }).catch(error => {
        //     console.error("Ошибка при получении данных о пользователе:", error);
        //     logOut(); // Если произошла ошибка, выходим из системы
        // });
    }, []);

    const login = async (usernameAndPassword) => {
        return new Promise((resolve, reject) => {
            performLogin(usernameAndPassword).then(res => {
                const jwtToken = res.headers["authorization"];
                localStorage.setItem("access_token", jwtToken);
                const decodedToken = jwtDecode(jwtToken)
                console.log(jwtToken);
                setTeacherFromToken();
                // fetchTeacherData().then(userData => {
                //     console.log("Данные пользователя:", userData[1]); // Добавьте это для отладки
                //     setPerson({
                //         username: decodedToken.sub,
                //         avatarUrl: userData[1].avatarUrl, // Предположим, что сервер возвращает это поле
                //         roles: userData[1].role
                //     });
                    resolve(res); // Возвращаем результат
                }).catch(error => {
                    console.error("Ошибка при получении данных о пользователе:", error);
                    reject(error); // Обрабатываем ошибку
                });

            //     const decodedToken = jwtDecode(jwtToken)
            // setPerson({
            //     username: decodedToken.sub,
            //     roles: decodedToken.scopes
            // })
            //     resolve(res);
            // }).catch(err => {
            //         reject(err);
            // })
        })
    }

    const logOut = () => {
        localStorage.removeItem("access_token");
        setPerson(null);
    }

    
    const isUserAuthenticated = () => {
        const token = localStorage.getItem("access_token");
        if (!token) {
            return false;
        }
        const { exp: expiration }  = jwtDecode(token);
        if (Date.now > expiration * 1000) {
            logOut();
            return false;
        }
        return true;
    }

    return (
        <AuthContext.Provider value={{
            person,
            login,
            logOut,
            isUserAuthenticated,
            setTeacherFromToken
        }}>
            {children}

        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext);

export default AuthProvider;