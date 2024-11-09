import {Ups} from "../types/ups.ts";
import {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import FormBottom from "./FormBottom.tsx";
import NameAndAddressInputFields from "./NameAndAddressInputFields.tsx";
import { Server } from "../types/server.ts";
import "./UpsContent.css";
import {Status} from "../types/status.ts";
import StatusInfo from "./StatusInfo.tsx";

type EditProps = {
    ups: Ups,
    upsUpdate: () => void,
    servers: Server[],
    monitoring: boolean,
    getUpsStatus: (id: string) => Status | undefined
}


export default function UpsContentDisplayAndEditing(props: Readonly<EditProps>) {

    const [ups, setUps] = useState<Ups>({id:"new", name:"", address:"", community:""})
    const [servers, setServers] = useState<Server[]>([])
    const [editing, setEditing] = useState<boolean>(false)
    const [changedData, setChangedData] = useState<boolean>(false)
    const [nameInput, setNameInput] = useState<string>("")
    const [addressInput, setAddressInput] = useState<string>("")
    const [communityInput, setCommunityInput] = useState<string>("")
    const [message, setMessage] = useState<string>("")          // in case of errors and for warning before deletion
    const confirmationMessage: string = "Really delete? Reclick button to confirm."
    const navigate = useNavigate()
    const status: Status|undefined = props.getUpsStatus(ups.id)

    function getClassName (): string {
        if (props.monitoring) {
            if (status?.state === "POWER_OFF")
                return "ups-card-poweroff"
            if (status?.state === "POWER_OFF_LIMIT" || status?.state === "SHUTDOWN")
                return "ups-card-shutdown"
        }
        return "ups-card"
    }

    const switchEditMode = (state:boolean) => {
        setEditing(state)
        setChangedData(false)
        setMessage("")
    }

    function backToList(updated:boolean) {
        switchEditMode(false)
        if (updated) props.upsUpdate()
        navigate("/")
        //history.back()
    }

    /** initialize data from props */
    useEffect(() => {
        setUps(props.ups)
        setServers(props.servers.filter(server => server.upsId === props.ups.id)
    )
        setEditing(props.ups.id === "new")
        setNameInput(props.ups.name)
        setAddressInput(props.ups.address)
        setCommunityInput(props.ups.community)
    }, [props.ups, props.servers])
    

    function resetForm(){
        setNameInput(ups.name)
        setAddressInput(ups.address)
        setCommunityInput(ups.community)
        setChangedData(false)
    }

    function testConnection() {
        alert("Test initiated")
    }

    /** axios calls ****************************************************************************************/
    function addUps() : void {
        axios.post('/api/ups', {name: nameInput, address: addressInput, community: communityInput})
            .then(response => {
                if (response.status == 200) backToList(true);
                else setMessage(response.data);
            })
            .catch(error => {
                console.error('Creating new UPS failed:', error);
            });
    }

    function updateUps() : void {
        axios.put('/api/ups/' + ups.id, {name: nameInput, address: addressInput, community: communityInput})
            .then(response => {
                if (response.status == 200){
                    backToList(true)
                }
                else setMessage(response.data)
            })
            .catch(error => {
                console.error('Updating UPS failed:', error);
            });
    }

    function deleteUps(): void {
        axios.delete('/api/ups/' + ups.id)
            .then(response => {
                if (response.status == 200) {
                    backToList(true)
                }
                else setMessage(response.data)
            })
            .catch(error => {
                console.error('Deleting UPS failed:', error);
            });
    }
    /** end axios calls ***************************************************************************************/

    function submitEditForm(): void {
        if (!addressInput) {    // input error
            setMessage("Error: Address is mandatory")
            return
        }
        if (props.monitoring && addressInput!==ups.address && ups.id!=="new") {
            setMessage("Error: Address cannot be changed in monitoring mode")
            return
        }
        if (props.monitoring && communityInput!==ups.community && ups.id!=="new") {
            setMessage("Error: Address cannot be changed in monitoring mode")
            return
        }
        if (ups.id==="new") {
            addUps()
        } else {
            updateUps()
        }
    }

    function deleteClicked(): void {
        if (message === confirmationMessage) {
            deleteUps()
        } else
            setMessage(confirmationMessage)
    }

    const communityInputField = <input
        id={'community'}
        type={'text'}
        name={'community'}
        value={communityInput}
        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
            setCommunityInput(event.target.value)
            setChangedData(true)
        }}
    />;

    return (
        <>
            <h3>Details of {ups.name} &nbsp;
                <button onClick={() => backToList(false)}>
                    Back to List
                </button>
            </h3>
            <form name={"edit"}>
                <ul className={getClassName()}>
                <NameAndAddressInputFields editing={editing} name={ups.name} nameInput={nameInput}
                                               setNameInput={setNameInput}
                                               address={ups.address} addressInput={addressInput}
                                               setAddressInput={setAddressInput}
                                               setChangedData={setChangedData}/>
                    <li>
                        <label className={"description"} htmlFor={'community'}>Community String:</label>
                        {editing
                            ? communityInputField
                            : <p className={"value"} >{ups.community}</p>}
                    </li>
                    <li>
                        <button id={"testbutton"} type={"button"} hidden={!editing} onClick={() => testConnection()}>
                            Connection Test
                        </button>
                    </li>
                    <FormBottom resetForm={resetForm} changedData={changedData} submitEditForm={submitEditForm}
                                deleteClicked={deleteClicked} editing={editing} switchEditMode={switchEditMode}
                                message={message} confirmationMessage={confirmationMessage}/>
                    <li>
                        <p className={"description"}>Servers connected:</p>
                    </li>
                    {servers.map(server =>
                        <li key={server.id} className={"serverline"}><a
                            href={"/server/" + server.id}> {server.name} ({server.address}) </a></li>
                    )}
                    {props.monitoring && (ups.id!=="new")
                        &&
                        <li><StatusInfo status={status}/></li>
                    }
                </ul>
            </form>
        </>
    )
}

