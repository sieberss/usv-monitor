import {Credentials} from "../types/credentials.ts";
import {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

type EditProps = {
    credentials: Credentials
    credentialsUpdate: () => void
}


export default function CredentialsContentDisplayAndEditing(props: Readonly<EditProps>) {

    const [credentials, setCredentials] = useState<Credentials>({id:"new", user:"", password:"", global:true})
    const [editing, setEditing] = useState<boolean>(false)
    const [changedData, setChangedData] = useState<boolean>(false)
    const [userInput, setUserInput] = useState<string>("")
    const [passwordInput, setPasswordInput] = useState<string>("")
    const [message, setMessage] = useState<string>("")          // in case of errors and for warning before deletion
    const confirmationMessage: string = "Really delete? Reclick button to confirm."
    const navigate = useNavigate()

    const switchEditMode = (state:boolean) => {
        setEditing(state)
        setChangedData(false)
        setMessage("")
    }

    function backToList(updated:boolean) {
        switchEditMode(false)
        if (updated) props.credentialsUpdate()
        navigate("/credentials")
    }

    useEffect(() => {
        setCredentials(props.credentials)
        setEditing(props.credentials.id === "new")
        setUserInput(props.credentials.user)
        setPasswordInput(props.credentials.password)
    }, [props.credentials])

    function setInputStartValues(){
        setUserInput(credentials.user)
        setPasswordInput(credentials.password)
    }
    function resetForm() {
        setInputStartValues()
        setChangedData(false)
    }

    function submitEditForm(): void {
        if (!userInput || !passwordInput) {    // input error
            setMessage("Error: Username and password are mandatory")
            return
        }
        if (credentials.id==="new") {
            axios.post('/api/credentials', {user: userInput, password: passwordInput, global: true})
                .then(response => {
                    if (response.status == 200) backToList(true);
                    else setMessage(response.data);
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        } else {                // updating an existing credentials
            axios.put('/api/credentials/' + credentials.id, {user: userInput, password: passwordInput, global: true})
                .then(response => {
                    if (response.status == 200){
                        backToList(true)
                    }
                    else setMessage(response.data)
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        }
    }

    function deleteCredentials(): void {
        axios.delete('/api/credentials/' + credentials.id)
            .then(response => {
                if (response.status == 200) {
                    backToList(true)
                }
                else setMessage(response.data)
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }

    function deleteClicked(): void {
        if (message === confirmationMessage) {
            deleteCredentials()
        } else
            setMessage(confirmationMessage)
    }

    const userInputField = <input
        id={'user'}
        type={'text'}
        name={'user'}
        value={userInput}
        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
            setUserInput(event.target.value)
            setChangedData(true)
        }}
    />;

    const passwordInputField = <input
        id={'password'}
        type={'password'}
        name={'password'}
        value={passwordInput}
        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
            setPasswordInput(event.target.value)
            setChangedData(true)
        }}
    />;


    return (
        <>
            <h3>Details of credentials</h3>
            <button onClick={() => backToList(false)} >
                Back
            </button>

            <form name={"edit"}>
                <ul>
                    <li>
                        <label htmlFor={'user'}>User:</label>
                        {editing
                            ? userInputField
                            : <p>{credentials.user}</p>}
                    </li>
                    <li>
                        <label htmlFor={'password'}>Password:</label>
                        {editing
                            ? passwordInputField
                            : <p>********</p>}
                    </li>
                    <li></li>
                    <li>
                        <button id={"reset"} type={"button"} onClick={() => {
                            resetForm()
                        }} hidden={!changedData}>
                            Reset
                        </button>
                        <button id={"submit"} type={"button"} onClick={() => submitEditForm()} hidden={!changedData}>
                            Save
                        </button>
                    </li>
                    <li>
                        <button type={"button"} onClick={deleteClicked} hidden={editing}>
                            Delete
                        </button>
                        <button type={"button"} onClick={() => switchEditMode(true)} hidden={editing || message===confirmationMessage}>
                            Edit
                        </button>
                    </li>
                    <p>{message}</p>
                </ul>
            </form>
        </>
    )
}
