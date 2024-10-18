import {Usv} from "../types/usv.ts";


import germanData from 'german.json'
import englishData from 'english.json'

type GermanTexts = typeof germanData
type EnglishTexts = typeof englishData

type DetailsProos = {
    usv : Usv
}

export default function UsvDetails(props:Readonly<DetailsProos>){
    const switchToEdit = () => {alert("Edit mode")}
    return (
        <>
            <p>Detailansicht, zum Editieren Doppelklick auf den Wert </p>
            <table align={"left"}>
                <tbody>
                <tr>
                    <td className={"description"}>Name</td>
                    <td className={"value"} onDoubleClick={() => switchToEdit()}> {props.usv.name}</td>
                </tr>
                <tr>
                    <td className={"description"}>Adresse</td>
                    <td className={"value"} onDoubleClick={() => switchToEdit()}> {props.usv.address}</td>
                </tr>
                <tr>
                    <td className={"description"}>Community</td>
                    <td className={"value"} onDoubleClick={() => switchToEdit()}> {props.usv.community}</td>
                </tr>
                </tbody>
            </table>
        </>
    )
}

