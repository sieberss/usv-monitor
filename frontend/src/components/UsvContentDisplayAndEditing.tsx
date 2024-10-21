import {Usv} from "../types/usv.ts";
import {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

type EditProps = {
    usv: Usv
    usvUpdate: () => void
}


export default function UsvContentDisplayAndEditing(props: Readonly<EditProps>) {

    const [usv, setUsv] = useState<Usv>(props.usv)
    const [editing, setEditing] = useState<boolean>(props.usv.id==="new")      // start in edit mode only for new entry
    const [changedData, setChangedData] = useState<boolean>(false)
    // editing starts with empty input fields for new entry, otherwise filled with old values
    const [nameInput, setNameInput] = useState<string>(props.usv.name)
    const [addressInput, setAddressInput] = useState<string>(props.usv.address)
    const [communityInput, setCommunityInput] = useState<string>(props.usv.community)
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
        if (updated) props.usvUpdate()
        navigate("/")
    }

    useEffect(() => {
        setUsv(props.usv)
        setEditing(props.usv.id === "new")
        setNameInput(props.usv.name)
        setAddressInput(props.usv.address)
        setCommunityInput(props.usv.community)
    }, [props.usv])

    function setInputStartValues(){
        setNameInput(usv.name)
        setAddressInput(usv.address)
        setCommunityInput(usv.community)
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
        if (usv.id==="new") {
            axios.post('/api/usv', {name: nameInput, address: addressInput, community: communityInput})
                .then(response => {
                    if (response.status == 200) backToList(true);
                    else setMessage(response.data);
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        } else {                // updating an existing UPS
            axios.put('/api/usv/' + usv.id, {name: nameInput, address: addressInput, community: communityInput})
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

    function deleteUsv(): void {
        axios.delete('/api/usv/' + usv.id)
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
            deleteUsv()
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
                            : <p>{usv.name}</p>}
                    </li>
                    <li>
                        <label htmlFor={'address'}>Adresse (IP oder FQDN):</label>
                        {editing
                            ? addressInputField
                            : <p>{usv.address}</p>}
                    </li>
                    <li>
                        <label htmlFor={'community'}>Community-String:</label>
                        {editing
                            ? communityInputField
                            : <p>{usv.community}</p>}
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

