import {Ups} from "../types/ups.ts";
import {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import { Server } from "../types/server.ts";
import { Credentials } from "../types/credentials.ts";

type EditProps = {
    server: Server,
    upses: Ups[],
    credentialsList: Credentials[],
    serverUpdate: () => void
}


export default function UpsContentDisplayAndEditing(props: Readonly<EditProps>) {

    const [server, setServer] = useState<Server>({id: "new", name: "", address: "", upsId: "", shutdownTime: 180, credentials:{id:"", user:"", password:"", global:false}})
    const [editing, setEditing] = useState<boolean>(false)
    const [changedData, setChangedData] = useState<boolean>(false)
    const [nameInput, setNameInput] = useState<string>("")
    const [addressInput, setAddressInput] = useState<string>("")
    const [userInput, setUserInput] = useState<string>("")
    const [passwordInput, setPasswordInput] = useState<string>("")
    const [upsSelection, setUpsSelection] = useState<string>("")
    const [changedCredentialsInput, setChangedCredentialsInput] = useState<boolean>(false)
    const [credentialsLocalSelected, setCredentialsLocalSelected] = useState<boolean>(false)
    const [globalCredentialsSelection, setGlobalCredentialsSelection] = useState<Credentials>()
    const [shutdownMinutesInput, setShutdownMinutesInput] = useState<number>(3)
    const [message, setMessage] = useState<string>("")          // in case of errors and for warning before deletion
    const confirmationMessage: string = "Really delete? Reclick button to confirm."
    const navigate = useNavigate()

    function switchEditMode(state:boolean) : void {
        setEditing(state)
        setChangedData(false)
        setChangedCredentialsInput(false)
        setMessage("")
    }

    function backToList(updated:boolean) : void {
        switchEditMode(false)
        if (updated) props.serverUpdate()
        navigate("/server")
    }

    /** initialize data from props */
    useEffect(() => {
        setServer(props.server)
        setEditing(props.server.id === "new")
        setNameInput(props.server.name)
        setAddressInput(props.server.address)
        if (props.server.credentials.global){
            setCredentialsLocalSelected(false)
            setGlobalCredentialsSelection(props.server.credentials)
            setUserInput("")
            setPasswordInput("")
        }
        else {
            setUserInput(props.server.credentials.user)
            setPasswordInput(props.server.credentials.password)
            setCredentialsLocalSelected(true)
            setGlobalCredentialsSelection(undefined)
        }
        setUpsSelection(props.server.upsId)
        setShutdownMinutesInput(props.server.shutdownTime / 60)
    }, [props.server])

    /** reset form data to content of server object */
    function resetForm(){
        setNameInput(server.name)
        setAddressInput(server.address)
        if (server.credentials.global) {
            setCredentialsLocalSelected(false)
            setGlobalCredentialsSelection(props.server.credentials)
            setUserInput("")
            setPasswordInput("")
        }
        else {
            setCredentialsLocalSelected(true)
            setUserInput(server.credentials.user)
            setPasswordInput(server.credentials.password)
        }
        setUpsSelection(server.upsId)
        setShutdownMinutesInput(server.shutdownTime / 60)
        setChangedData(false)
        setChangedCredentialsInput(false)
    }

    /** axios calls for credentials *********************************************************************************/

    /** @return new Credentials object from backend with provided data in case of success,
     *          null in case of failure
     */
    function addLocalCredentials() : Credentials|null {
        axios.post('api/credentials', {user: userInput, password: passwordInput, global: false})
            .then(response => {
                if (response.status == 200) {
                    return response.data
                }
                else {
                    setMessage(response.data)
                    return null
                }
            })
            .catch(error => {
                console.error("addLocalredentials failed:", error)
                return null
            })
        return null
    }

    /** @return true if update was successful, false otherwise */
    function updateLocalCredentials() : boolean {
        axios.post('api/credentials/' + server.credentials.id, {user: userInput, password: passwordInput, global: false})
            .then(response => {
                if (response.status == 200) {
                    return true
                }
                else {
                    setMessage(response.data)
                    return false
                }
            })
            .catch(error => {
                console.error("updateLocalredentials failed:", error)
                return false
            })
        return false
    }

    /** @return true if delete was successful, false otherwise */
    function deleteLocalCredentials(): boolean {
        axios.delete('/api/credentials/' + server.credentials.id)
            .then(response => {
                if (response.status == 200) {
                    return true
                }
                else {
                    setMessage(response.data)
                    return false
                }
            })
            .catch(error => {
                console.error('deleteLocalCredentials failed:', error);
                return false
            });
        return false
    }

    /** preparation of embedded credentials object before server object can be stored **************************************/
    function buildEmbeddedCredentialsObject(): Credentials|null {
        if (credentialsLocalSelected) {
            if (server.id === "new" || server.credentials.global) {
                /** when for a new server local Credentials are chosen
                 * or an existing server changes from global to local credentials
                 *  a new Credentials object has to be created first
                 */
                return addLocalCredentials()
            } else if (changedCredentialsInput) {
                /** local credentials are changed, need to be updated in database first,
                 * in case of failure keep old data */
                return updateLocalCredentials()
                    ? {
                        id: server.credentials.id,
                        user: userInput,
                        password: passwordInput,
                        global: false}
                    : server.credentials
            }
            else /** no changes to local credentials */
                 return server.credentials
        }
        else
            /** when global credentials are chosen, data is found in globalCredentialsSelection
             * (if this should be undefined return null)
             */
            return globalCredentialsSelection ? globalCredentialsSelection : null
    }

    /** axios calls for server ********************************************************************/
    function addServer():void{
        const credentialsToStore : Credentials|null = buildEmbeddedCredentialsObject()
        axios.post('/api/server', {
            name: nameInput,
            address: addressInput,
            credentials: credentialsToStore,
            upsId: upsSelection,
            shutdownTime: Math.round(shutdownMinutesInput * 60)
        })
            .then(response => {
                if (response.status == 200)
                    backToList(true);
                else setMessage(response.data);
            })
            .catch(error => {
                console.error('addServer failed:', error);
            });
    }

    function updateServer():void {
        const credentialsToStore = buildEmbeddedCredentialsObject()
        axios.put('/api/server/' + server.id, {
            name: nameInput,
            address: addressInput,
            credentials: credentialsToStore,
            upsId: upsSelection,
            shutdownTime: Math.round(shutdownMinutesInput * 60)
        })
            .then(response => {
                if (response.status == 200){
                    /** delete localCredentials if not contained in updated server object */
                    if ((credentialsToStore===null || credentialsToStore.global) && !server.credentials.global)
                        deleteLocalCredentials()
                    backToList(true)
                }
                else setMessage(response.data)
            })
            .catch(error => {
                console.error('updateServer failed:', error);
            });
    }

    function deleteServer(): void {
        axios.delete('/api/server/' + server.id)
            .then(response => {
                if (response.status == 200) {
                    /** local credentials for a deleted server can be deleted */
                    if (!server.credentials.global)
                        deleteLocalCredentials()
                    backToList(true)
                }
                else setMessage(response.data)
            })
            .catch(error => {
                console.error('deleteServer failed:', error);
            });
    }
    /** end axios calls for server *************************************************************/

    function submitEditForm(): void {
        if (!addressInput) {    // input error
            setMessage("Error: Address is mandatory")
            return
        }
        if (credentialsLocalSelected && (userInput==="" || passwordInput==="")){
            setMessage("For local users username and password must be entered")
            return
        }
        if (shutdownMinutesInput < 0) {
            setMessage("Time for Shutdown cannot be negative")
            return;
        }
        if (server.id==="new") {
            addServer()
        }
        else {
            updateServer()
        }
    }

    function deleteClicked(): void {
        if (message === confirmationMessage) {
            deleteServer()
        } else
            setMessage(confirmationMessage)
    }

    const nameInputField = <input
        id={'name'}
        type={'text'}
        name={'name'}
        value={nameInput}
        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
            setNameInput(event.target.value)
            setChangedData(true)
        }}
    />;

    const addressInputField = <input
        id={'address'}
        type={'text'}
        name={'address'}
        value={addressInput}
        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
            setAddressInput(event.target.value)
            setChangedData(true)
        }}
    />;

    const userInputField = <input
        id={'user'}
        type={'text'}
        name={'user'}
        value={userInput}
        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
            setUserInput(event.target.value)
            setChangedData(true)
            setChangedCredentialsInput(true)
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
            setChangedCredentialsInput(true)
        }}
    />;

    const shutdownMinutesField = <input
        id={'minutes'}
        type={'number'}
        name={'minutes'}
        value={shutdownMinutesInput}
        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
            setShutdownMinutesInput(parseFloat(event.target.value))
            setChangedData(true)
            setChangedCredentialsInput(true)
        }}
    />;

    return (
        <>
            <h3>Details of Server</h3>
            <button onClick={() => backToList(false)} >
                Back
            </button>

            <form name={"edit"}>
                <ul>
                    <li>
                        <label htmlFor={'name'}>Name:</label>
                        {editing
                            ? nameInputField
                            : <p>{server.name}</p>}
                    </li>
                    <li>
                        <label htmlFor={'address'}>Address (IP or FQDN):</label>
                        {editing
                            ? addressInputField
                            : <p>{server.address}</p>}
                    </li>
                    <li>
                        <label htmlFor={'user'}>Username:</label>
                        {editing
                            ? userInputField
                            : <p>{server.credentials.user}</p>}
                    </li>
                    <li>
                        <label htmlFor={'password'}>Password:</label>
                        {editing
                            ? passwordInputField
                            : <p>{server.credentials.user}</p>}
                    </li>
                    <li>
                        <label htmlFor={'minutes'}>Remaining battery time for shutdown (minutes):</label>
                        {editing
                            ? shutdownMinutesField
                            : <p>{server.credentials.user}</p>}
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


