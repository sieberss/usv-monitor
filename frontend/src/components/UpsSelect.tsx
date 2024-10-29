import {Ups} from "../types/ups"

type Props = {
    disabled: boolean
    selection: string,
    setSelected: (selection: string) => void,
    setChangedData: (changed: boolean) => void,
    upsList: Ups[]
}

export default function UpsSelect(props: Readonly<Props>) {
    return (
        <>
            <select name={"ups"} disabled={props.disabled} onChange={(event: React.ChangeEvent<HTMLSelectElement>) => {
                props.setChangedData(true)
                props.setSelected(event.target.value)
            }}>
                {props.upsList.map(c =>
                    <option value={c.id} selected={c.id === props.selection}> {c.name} ({c.address}) </option>)}
            </select>
        </>
    )
}