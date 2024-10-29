import {Credentials} from "../types/credentials"

type Props = {
    disabled: boolean,
    selection: string,
    setSelected: (selection: string) => void,
    setChangedData: (changed: boolean) => void,
    credentialsList: Credentials[]
}

export default function CredentialsSelect(props: Readonly<Props>) {
    return (
        <>
            <select name={"credentials"} disabled={props.disabled} onChange={(event: React.ChangeEvent<HTMLSelectElement>) => {
                props.setChangedData(true)
                props.setSelected(event.target.value)
            }}>
                {props.credentialsList.map(c =>
                    <option value={c.id} selected={c.id === props.selection}> {c.user} </option>)}
            </select>
        </>
    )
}