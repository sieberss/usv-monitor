import {Ups} from "../types/ups.ts";
import {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

type EditProps = {
    ups: Ups
    upsUpdate: () => void
}


export default function UpsContentDisplayAndEditing(props: Readonly<EditProps>) {

    const [ups, setUps] = useState<Ups>({id:"new", name:"", address:"", community:""})
    const [editing, setEditing] = useState<boolean>(false)
    const [changedData, setChangedData] = useState<boolean>(false)
    const [nameInput, setNameInput] = useState<string>("")
    const [addressInput, setAddressInput] = useState<string>("")
    const [communityInput, setCommunityInput] = useState<string>("")
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
        if (updated) props.upsUpdate()
        navigate("/")
    }

    /** initialize data from props */
    useEffect(() => {
        setUps(props.ups)
        setEditing(props.ups.id === "new")
        setNameInput(props.ups.name)
        setAddressInput(props.ups.address)
        setCommunityInput(props.ups.community)
    }, [props.ups])

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
            <h3>Details of UPS</h3>
            <button onClick={() => backToList(false)} >
                Back
            </button>

            <form name={"edit"}>
                <ul>
                    <li>
                        <label htmlFor={'name'}>Name:</label>
                        {editing
                            ? nameInputField
                            : <p>{ups.name}</p>}
                    </li>
                    <li>
                        <label htmlFor={'address'}>Address (IP or FQDN):</label>
                        {editing
                            ? addressInputField
                            : <p>{ups.address}</p>}
                    </li>
                    <li>
                        <label htmlFor={'community'}>Community String:</label>
                        {editing
                            ? communityInputField
                            : <p>{ups.community}</p>}
                    </li>
                    <li>
                        <button id={"testbutton"} type={"button"} hidden={!editing} onClick={() => testConnection()}>
                            Connection Test
                        </button>
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

