import {Usv} from "../types/usv.ts";
import {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

type EditProps = {
    usv: Usv
}

export default function UsvDetails(props: Readonly<EditProps>) {
    const [editing, setEditing] = useState<boolean>(!props.usv) // start in edit mode only for new entry
    const [changedData, setChangedData] = useState<boolean>(false)
    // editing starts with empty input fields for new entry, otherwise filled with old values
    const nameStartValue = props.usv ? props.usv.name : ""
    const addressStartValue = props.usv ? props.usv.address : ""
    const communityStartValue = props.usv ? props.usv.community : ""
    const [nameInput, setNameInput] = useState<string>(nameStartValue)
    const [addressInput, setAddressInput] = useState<string>(addressStartValue)
    const [communityInput, setCommunityInput] = useState<string>(communityStartValue)
    const [message, setMessage] = useState<string>("") // in case of errors and for warning before deletion
    const confirmationMessage: string = "Soll diese Anlage wirklich gelöscht werden? Bestätigen durch erneuten Klick auf den Button"
    const navigate = useNavigate()

    function testConnection() {
        alert("Test angestoßen")
    }

    function submitEditForm(): void {
        if (!addressInput) {  // input error
            setMessage("Fehler: Adresse muss angegeben werden")
            return
        }
        if (!props.usv) { // adding a new UPS
            axios.post('/api/usv/', {name: nameInput, address: addressInput, community: communityInput})
                .then(response => {
                    if (response.status == 200) {
                        setEditing(false)
                        setMessage("")
                    } else setMessage(response.data);
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        } else { // updating an existing UPS
            axios.put('/api/usv/' + props.usv.id, {name: nameInput, address: addressInput, community: communityInput})
                .then(response => {
                    if (response.status == 200)
                        setEditing(false)
                    else setMessage(response.data)
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        }
    }

    function deleteUsv(): void {
        axios.delete('/api/usv/' + props.usv.id)
            .then(response => {
                if (response.status == 200)
                    navigate("/")
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

    const cancelButton = <button onClick={() => navigate("/usvdetails/" + props.usv.id)}>
        Abbrechen
    </button>;
    const backButton = <button onClick={() => navigate("/")}>
        Zurück
    </button>;

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

    const testResetAndSubmitButtons = <>
        <li>
            <button id={"testbutton"} type={"button"}
                    onClick={() => testConnection()}>Verbindungstest
            </button>
        </li>
        {changedData // reset and submit button only visible after change
            && <>
                <li></li>
                <li>
                    <button id={"reset"} type={"button"} onClick={() => {
                        setNameInput(nameStartValue)
                        setAddressInput(addressStartValue)
                        setCommunityInput(communityStartValue)
                        setChangedData(false)
                    }}>
                        Reset
                    </button>
                    <button id={"submit"} type={"button"} onClick={() => submitEditForm()}>
                        Speichern
                    </button>
                </li>
            </>
        }
    </>;

    const editAndDeleteButtons = <>
        <button onClick={deleteClicked}>
            Löschen
        </button>
        <button onClick={() => {
            setEditing(true);
            setMessage("")
        }}>
            Bearbeiten
        </button>
    </>;


    return (
        <>
            <h3>Daten der Anlage</h3>
            {editing ? cancelButton : backButton}
            <form name={"edit"}>
                <ul>
                    <li>
                        <label htmlFor={'name'}>Name:</label>
                        {editing
                            ? nameInputField
                            : <p>{props.usv.name}</p>}
                    </li>
                    <li>
                        <label htmlFor={'address'}>Adresse (IP oder FQDN):</label>
                        {editing
                            ? addressInputField
                            : <p>{props.usv.address}</p>}
                    </li>
                    <li>
                        <label htmlFor={'community'}>Community-String:</label>
                        {editing
                            ? communityInputField
                            : <p>{props.usv.community}</p>}
                    </li>
                    {editing ? testResetAndSubmitButtons : editAndDeleteButtons}
                    <p>{message}</p>
                </ul>
            </form>
        </>
    )
}

