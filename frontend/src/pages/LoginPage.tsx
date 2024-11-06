import axios from "axios";
import {useState} from "react";
import {useNavigate} from "react-router-dom";

type Props = {
    setMenuItem: (item:string) => void,
    appUserExists: boolean,
    credentialsUpdateOccured: () => void,
    setUsername: (username: string) => void,
    adminUser: string
}

export default function LoginPage (props:Readonly<Props>){
    props.setMenuItem("login")
    const [message, setMessage] = useState<string>("")
    const [password, setPassword] = useState<string>("")
    const navigate = useNavigate()

    const checkLogin = () => {
        axios.post('/api/login', undefined, {auth: {"username": props.adminUser, "password": password}})
            .then(r => {
                const successful = r.data === props.adminUser
                if (successful) {
                    props.setUsername(props.adminUser)
                    setMessage("")
                    navigate("/")
                }
                else
                    setMessage("Login failed")
            })
            .catch(error => {
                setMessage("Error in login process")
                console.error('Login error:', error);
            });
    }

    const register = () => {
        axios.post('api/login/register', {user: props.adminUser, password: password})
            .then(r => {
                if (r.status == 200) {
                    props.credentialsUpdateOccured()
                    checkLogin()
                }
                else
                    setMessage("Could not create user")
            })
            .catch(error => {
                setMessage("Error in register process")
                console.error("Register error:", error)
            })
    }

    function submitPassword(e:React.FormEvent) {
        e.preventDefault()
        if (props.appUserExists)
            checkLogin()
        else if (password.length < 8) {
            setMessage("Password must have at least 8 characters")
        }
        else {
            register()
        }
    }

    return(
            <form name={"login"} onSubmit={submitPassword}>
                <h2>Welcome to your UPS Monitor.</h2>
                <p>Please enter your password</p>
                <input type={"password"} id={"password"} name={"password"} value={password}
                       onChange={(event: React.ChangeEvent<HTMLInputElement>) => setPassword(event.target.value)} />
                <button> Submit </button>
                <p className={"message"}>{message}</p>
            </form>

    )
}