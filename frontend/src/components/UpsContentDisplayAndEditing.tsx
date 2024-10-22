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
    const confirmationMessage: string = "Soll diese Anlage wirklich gelöscht werden? Bestätigen durch erneuten Klick auf den Button"
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

    useEffect(() => {
        setUps(props.ups)
        setEditing(props.ups.id === "new")
        setNameInput(props.ups.name)
        setAddressInput(props.ups.address)
        setCommunityInput(props.ups.community)
    }, [props.ups])

    function setInputStartValues(){
        setNameInput(ups.name)
        setAddressInput(ups.address)
        setCommunityInput(ups.community)
    }
    function resetForm() {
        setInputStartValues()
        setChangedData(false)
    }

    function testConnection() {
        alert("Test angestoßen")
    }

    function submitEditForm(): void {
        if (!addressInput) {    // input error
            setMessage("Fehler: Adresse muss angegeben werden")
            return
        }
        if (ups.id==="new") {
            axios.post('/api/ups', {name: nameInput, address: addressInput, community: communityInput})
                .then(response => {
                    if (response.status == 200) backToList(true);
                    else setMessage(response.data);
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        } else {                // updating an existing UPS
            axios.put('/api/ups/' + ups.id, {name: nameInput, address: addressInput, community: communityInput})
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

    function deleteUps(): void {
        axios.delete('/api/ups/' + ups.id)
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
            <h3>Daten der Anlage</h3>
            <button onClick={() => backToList(false)} >
                Zurück
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
                        <label htmlFor={'address'}>Adresse (IP oder FQDN):</label>
                        {editing
                            ? addressInputField
                            : <p>{ups.address}</p>}
                    </li>
                    <li>
                        <label htmlFor={'community'}>Community-String:</label>
                        {editing
                            ? communityInputField
                            : <p>{ups.community}</p>}
                    </li>
                    <li>
                        <button id={"testbutton"} type={"button"} hidden={!editing} onClick={() => testConnection()}>
                            Verbindungstest
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
                            Speichern
                        </button>
                    </li>
                    <li>
                        <button type={"button"} onClick={deleteClicked} hidden={editing}>
                            Löschen
                        </button>
                        <button type={"button"} hidden={editing} onClick={() => switchEditMode(true)}>
                            Bearbeiten
                        </button>
                    </li>
                    <p>{message}</p>
                </ul>
            </form>
        </>
    )
}

