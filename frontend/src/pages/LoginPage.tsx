import axios from "axios";
import {useState} from "react";
import {useNavigate} from "react-router-dom";

type Props = {
    setLoggedIn: (newState: boolean) => void
}

export default function LoginPage (props:Readonly<Props>){
    const [message, setMessage] = useState<string>("")
    const [password, setPassword] = useState<string>("")
    const navigate = useNavigate()

    const checkLogin = () => {
        axios.post('/api/login', {password})
            .then(response => {
                if (response.status == 200 && response.data.loggedIn){
                    props.setLoggedIn(true)
                    setMessage("")
                    navigate("/")
                }
                else setMessage("Password was wrong")
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }

    function submitPassword() {
        if (password.length < 8)
            setMessage("Password must have at least 8 characters")
        else {
            checkLogin()
        }
    }

    return(

            <form name={"login"}>
                <p>Welcome to your UPS Monitor.</p>
                <p>Please enter your password</p>
                <input type={"password"} id={"password"} name={"password"} value={password}
                       onChange={(event: React.ChangeEvent<HTMLInputElement>) => setPassword(event.target.value)} />
                <button type={"button"} onClick={submitPassword}/>
                <p>{message}</p>
            </form>

    )
}