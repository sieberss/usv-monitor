import {Ups} from "../types/ups.ts";
import {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {Server} from "../types/server.ts";
import {Credentials} from "../types/credentials.ts";
import CredentialsSelect from "./CredentialsSelect.tsx";
import UpsSelect from "./UpsSelect.tsx";
import FormBottom from "./FormBottom.tsx";
import NameAndAddressInputFields from "./NameAndAddressInputFields.tsx";
import CredentialsInfoline from "./CredentialsInfoline.tsx";
import UpsInfoline from "./UpsInfoline.tsx";
import "./ServerContent.css"

type EditProps = {
    server: Server,
    upses: Ups[],
    credentialsList: Credentials[],
    serverUpdate: () => void
}


export default function UpsContentDisplayAndEditing(props: Readonly<EditProps>) {

    const [server, setServer] = useState<Server>({
        id: "new",
        name: "",
        address: "",
        upsId: "",
        shutdownTime: 180,
        credentials: {id: "", user: "", password: "", global: false}
    })
    const [editing, setEditing] = useState<boolean>(false)
    const [changedData, setChangedData] = useState<boolean>(false)
    const [nameInput, setNameInput] = useState<string>("")
    const [addressInput, setAddressInput] = useState<string>("")
    const [upsSelection, setUpsSelection] = useState<string>("")
    const [changedCredentialsInput, setChangedCredentialsInput] = useState<boolean>(false)
    const [localSelected, setLocalSelected] = useState<boolean>(true)
    const [userInput, setUserInput] = useState<string>("")
    const [passwordInput, setPasswordInput] = useState<string>("")
    const [globalCredentialsSelection, setGlobalCredentialsSelection] = useState<string>("")
    const [shutdownSecondsInput, setShutdownSecondsInput] = useState<number>(3)
    const [message, setMessage] = useState<string>("")          // in case of errors and for warning before deletion
    const confirmationMessage: string = "Really delete? Reclick button to confirm."
    const navigate = useNavigate()

    function switchEditMode(state: boolean): void {
        setEditing(state)
        setChangedData(false)
        setChangedCredentialsInput(false)
        setMessage("")
    }

    function backToList(updated: boolean): void {
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
        if (!props.server.credentials || props.server.credentials.global) {
            setLocalSelected(false)
            setGlobalCredentialsSelection(props.server.credentials ?  props.server.credentials.id : "")
            setUserInput("")
            setPasswordInput("")
        } else {
            setLocalSelected(true)
            setGlobalCredentialsSelection("")
            setUserInput(props.server.credentials.user)
            setPasswordInput(props.server.credentials.password)
        }
        setUpsSelection(props.server.upsId)
        setShutdownSecondsInput(props.server.shutdownTime)
    }, [props.server])

    /** reset form data to content of server object */
    function resetForm() {
        setNameInput(server.name)
        setAddressInput(server.address)
        if (!server.credentials || server.credentials.global) {
            setLocalSelected(false)
            setGlobalCredentialsSelection(server.credentials ?  server.credentials.id : "")
            setUserInput("")
            setPasswordInput("")
        } else {
            setLocalSelected(true)
            setGlobalCredentialsSelection("")
            setUserInput(server.credentials.user)
            setPasswordInput(server.credentials.password)
        }
        setUpsSelection(server.upsId)
        setShutdownSecondsInput(server.shutdownTime)
        setChangedData(false)
        setChangedCredentialsInput(false)
    }

    function getGlobalCredentialsById(id: string): Credentials | undefined {
        return props.credentialsList.find(c => c.id === id)
    }


    /** axios calls  *********************************************************************************/

    function deleteLocalCredentials(): void {
        if (!server.credentials || server.credentials.global)  /** nothing needs to be deleted => success */
            return
        axios.delete('/api/credentials/' + server.credentials.id)
            .then(response => {
                if (response.status == 200) {
                    return true
                } else {
                    setMessage(response.data)
                    return false
                }
            })
            .catch(error => {
                console.error('deleteLocalCredentials failed:', error);
                return false
            });
    }

    function postToLocalEndpoint() : void {
        axios.post('/api/server/localcredentials', {
            name: nameInput,
            address: addressInput,
            user: userInput,
            password: passwordInput,
            upsId: upsSelection,
            shutdownTime: shutdownSecondsInput
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

    function postToGlobalEndpoint(){
        axios.post('/api/server', {
            name: nameInput,
            address: addressInput,
            credentials: getGlobalCredentialsById(globalCredentialsSelection),
            upsId: upsSelection,
            shutdownTime: shutdownSecondsInput
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

    function putToLocalEndpoint(){
        axios.put('/api/server/localcredentials/' + server.id, {
            name: nameInput,
            address: addressInput,
            user: userInput,
            password: passwordInput,
            upsId: upsSelection,
            shutdownTime: shutdownSecondsInput
        })
            .then(response => {
                if (response.status == 200) {
                    /** delete old localCredentials as endpoint generates new ones */
                    deleteLocalCredentials()
                    backToList(true)
                } else setMessage(response.data)
            })
            .catch(error => {
                console.error('updateServer failed:', error);
            });
    }

    function putToGlobalEndpoint(){
        axios.put('/api/server/' + server.id, {
            name: nameInput,
            address: addressInput,
            credentials: getGlobalCredentialsById(globalCredentialsSelection),
            upsId: upsSelection,
            shutdownTime: shutdownSecondsInput
        })
            .then(response => {
                if (response.status == 200) {
                    /** delete old localCredentials when changed to global */
                    deleteLocalCredentials()
                    backToList(true)
                } else setMessage(response.data)
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
                    deleteLocalCredentials()
                    backToList(true)
                } else setMessage(response.data)
            })
            .catch(error => {
                console.error('deleteServer failed:', error);
            });
    }

     function addServer(): void {
        if (localSelected)
            postToLocalEndpoint()
        else
            postToGlobalEndpoint()
    }

    function updateServer(): void {
        if (localSelected && changedCredentialsInput)
            putToLocalEndpoint()
        else
            putToGlobalEndpoint()
    }
    /** end axios calls for server *************************************************************/


    function submitEditForm(): void {
        if (!addressInput) {    // input error
            setMessage("Error: Address is mandatory")
            return
        }
        if (localSelected && (userInput === "" || passwordInput === "")) {
            setMessage("For local users username and password must be entered")
            return
        }
        if (shutdownSecondsInput < 0) {
            setMessage("Time for Shutdown cannot be negative")
            return;
        }
        if (server.id === "new") {
            addServer()
        } else {
            updateServer()
        }
    }

    function deleteClicked(): void {
        if (message === confirmationMessage) {
            deleteServer()
        } else
            setMessage(confirmationMessage)
    }

    const localCheckbox = <>
        <input
            id={"local"}
            name={"local"}
            type={"checkbox"}
            checked={localSelected}
            disabled={!editing}
            onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
                setLocalSelected(event.target.checked)
            }}
        />
        <label className={"description"} htmlFor={"local"}> use local credentials (only on this server)</label>
    </>;

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

    const localUserInput = <>
        <li>
            <label className={"description"} htmlFor={'user'}>Username:</label>
            {editing
                ? userInputField
                : <p className={"value"}>{server.credentials?.user}</p>}
        </li>
        <li>
            {editing && <>
                <label className={"description"} htmlFor={'password'}>Password:</label>
                {passwordInputField}
            </>}
        </li>
    </>;

    const globalUserInput =
        <li className={"credentials-infoline"}>
            <label className={"description"} htmlFor={"credentials"}>Global Credentials:</label>
            {editing
                ? <CredentialsSelect disabled={!editing} selection={globalCredentialsSelection}
                               setSelected={setGlobalCredentialsSelection}
                               setChangedData={setChangedData} credentialsList={props.credentialsList}/>
                : <CredentialsInfoline selection={globalCredentialsSelection} credentialsList={props.credentialsList} />
            }
        </li>

    const shutdownSecondsField = <input
        id={'seconds'}
        type={'number'}
        name={'seconds'}
        value={shutdownSecondsInput}
        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
            setShutdownSecondsInput(parseInt(event.target.value))
            setChangedData(true)
        }}
    />;

    function getClassName(server: Server) {
        return "server-content"
    }


    return (
        <>
            <h3>Details of {server.name} &nbsp;
                <button onClick={() => backToList(false)}>
                    Show List
                </button>
            </h3>

            <form name={"edit"}>
                <ul className={getClassName(server)}>
                    <NameAndAddressInputFields editing={editing} name={server.name} nameInput={nameInput} setNameInput={setNameInput}
                                               address={server.address} addressInput={addressInput} setAddressInput={setAddressInput}
                                               setChangedData={setChangedData}/>
                    <li>
                        {editing && localCheckbox}
                    </li>
                    {localSelected
                        ? localUserInput
                        : globalUserInput
                    }
                    <li className={"ups-infoline"}>
                        <label className={"description"} htmlFor={"ups"}>On UPS:</label>
                        {editing
                            ? <UpsSelect disabled={!editing} selection={upsSelection}
                                         setSelected={setUpsSelection}
                                         setChangedData={setChangedData} upsList={props.upses}/>
                            : <UpsInfoline selection={upsSelection} upsList={props.upses} />
                        }
                    </li>
                    <li>
                        <label className={"description"} htmlFor={'seconds'}>Shutdown Trigger (remaining battery time in seconds):</label>
                        {editing
                            ? shutdownSecondsField
                            : <p className={"value"}> {server.shutdownTime} </p>
                        }
                    </li>
                    <FormBottom resetForm={resetForm} changedData={changedData} submitEditForm={submitEditForm}
                                deleteClicked={deleteClicked} editing={editing} switchEditMode={switchEditMode}
                                message={message} confirmationMessage={confirmationMessage} />
                </ul>
            </form>
        </>
    )
}

