import {Usv} from "../types/usv.ts";


import germanData from 'german.json'
import englishData from 'english.json'
import {useState} from "react";

type GermanTexts = typeof germanData
type EnglishTexts = typeof englishData

type EditProps = {
    usv : Usv
}

export default function UsvDetails(props:Readonly<EditProps>){
    const nameStartValue = props.usv ? props.usv.name : ""
    const addressStartValue = props.usv ? props.usv.address : ""
    const communityStartValue = props.usv ? props.usv.community : ""
    const [nameInput, setNameInput] = useState<string>(nameStartValue)
    const [addressInput, setAddressInput] = useState<string>(addressStartValue)
    const [communityInput, setCommunityInput] = useState<string>(communityStartValue)
    function testConnection() {
        alert("Test angestoßen")
    }

    return (
        <>
        {props.usv ? <h3> USV editieren </h3> : <h3> Neue USV anlegen </h3> }
            <form name={"edit"}>
                <ul>
                    <li>
                        <label htmlFor={'name'}>Name:</label>
                        <input
                            id={'name'}
                            type={'text'}
                            name={'name'}
                            value={nameInput}
                            onChange={(event: React.ChangeEvent<HTMLInputElement>) => setNameInput(event.target.value)}
                        />
                    </li>
                    <li>
                        <label htmlFor={'address'}>Adresse (IP oder FQDN):</label>
                        <input
                            id={'address'}
                            type={'text'}
                            name={'address'}
                            value={addressInput}
                            onChange={(event: React.ChangeEvent<HTMLInputElement>) => setAddressInput(event.target.value)}
                        />
                    </li>
                    <li>
                        <label htmlFor={'community'}>Community-String:</label>
                        <input
                            id={'community'}
                            type={'text'}
                            name={'community'}
                            value={communityInput}
                            onChange={(event: React.ChangeEvent<HTMLInputElement>) => setCommunityInput(event.target.value)}
                        />
                    </li>
                    <li><button id={"testbutton"} type={"button"} onClick={() => testConnection()}>Verbindungstest</button></li>
                    <li></li>
                    <li><button id={"reset"} type={"button"} onClick={() => {
                        setNameInput(nameStartValue)
                        setAddressInput(addressStartValue)
                        setCommunityInput(communityStartValue)
                    }}>Reset</button> </li>

                </ul>
            </form>
        </>
    )
}

