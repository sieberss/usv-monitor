import {Ups} from "../types/ups"
import "./UpsSelect.css"

type Props = {
    disabled: boolean
    selection: string,
    setSelected: (selection: string) => void,
    setChangedData: (changed: boolean) => void,
    upsList: Ups[]
}

export default function UpsSelect(props: Readonly<Props>) {
    return (

        <select name={"ups"} value={props.selection} disabled={props.disabled} onChange={(event: React.ChangeEvent<HTMLSelectElement>) => {
            props.setChangedData(true)
            props.setSelected(event.target.value)
        }}>
            <option value={""}> ...</option>
            {props.upsList.map(ups =>
                <option className={"ups-select"} key={ups.id} value={ups.id}> {ups.name} ({ups.address}) </option>)}
        </select>

    )
}