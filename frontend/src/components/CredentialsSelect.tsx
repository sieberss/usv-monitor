import {Credentials} from "../types/credentials"
import "./CredentialsSelect.css"

type Props = {
    disabled: boolean,
    selection: string,
    setSelected: (selection: string) => void,
    setChangedData: (changed: boolean) => void,
    credentialsList: Credentials[]
}

export default function CredentialsSelect(props: Readonly<Props>) {
    return (

            <select name={"credentials"} value={props.selection} disabled={props.disabled} onChange={(event: React.ChangeEvent<HTMLSelectElement>) => {
                props.setChangedData(true)
                props.setSelected(event.target.value)
            }}>
                <option value={""}> ... </option>
                {props.credentialsList.map(c =>
                    <option key={c.id} value={c.id}> {c.user} </option>)}
            </select>

    )
}