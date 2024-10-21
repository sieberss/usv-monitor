import {Usv} from "../types/usv.ts";
import {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

type EditProps = {
    usv: Usv
}

export default function UsvDetails(props: Readonly<EditProps>) {
    const [usv, setUsv] = useState<Usv>({id:"new", name:"", address:"", community:""})          // are we adding a new UPS or displaying an existing one?
    //alert("hello") //usv.id + " " + props.usv.id")
    const [editing, setEditing] = useState<boolean>(false)      // start in edit mode only for new entry
    const [changedData, setChangedData] = useState<boolean>(false)
    // editing starts with empty input fields for new entry, otherwise filled with old values
    const [nameInput, setNameInput] = useState<string>()
    const [addressInput, setAddressInput] = useState<string>()
    const [communityInput, setCommunityInput] = useState<string>()
    const [message, setMessage] = useState<string>("")          // in case of errors and for warning before deletion
    const confirmationMessage: string = "Soll diese Anlage wirklich gelöscht werden? Bestätigen durch erneuten Klick auf den Button"
    const navigate = useNavigate()

    const switchEditMode = (state:boolean) => {
        setEditing(state)
        setMessage("")
    }

    useEffect(() => {
        setInputStartValues()
        setUsv(props.usv)
        setEditing(true)
        // alert(!props.usv.id)
    }, [])

    function setInputStartValues(){
        setNameInput(props.usv.name)
        setAddressInput(props.usv.address)
        setCommunityInput(props.usv.community)
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
        if (usv.id==="new") {          // adding a new UPS
            axios.post('/api/usv/', {name: nameInput, address: addressInput, community: communityInput})
                .then(response => {
                    if (response.status == 200) {
                        switchEditMode(false)
                        setUsv(response.data)
                    } else setMessage(response.data);
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        } else {                // updating an existing UPS
            axios.put('/api/usv/' + usv.id, {name: nameInput, address: addressInput, community: communityInput})
                .then(response => {
                    if (response.status == 200)
                        switchEditMode(false)
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

    const cancelEditButton = <button onClick={() => switchEditMode(false)}>
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
                    <button id={"reset"} type={"button"} onClick={() => {resetForm()}}>
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
        <button type={"button"} onClick={deleteClicked}>
            Löschen
        </button>
        <button type={"button"} onClick={() => {
            setEditing(true);
            setMessage("")
        }}>
            Bearbeiten
        </button>
    </>;


    return (
        <>
            <h3>Daten der Anlage</h3>
            {editing ? cancelEditButton : backButton}
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
                    {editing ? testResetAndSubmitButtons : editAndDeleteButtons}
                    <p>{message}</p>
                </ul>
            </form>
        </>
    )
}

