import {Ups} from "../types/ups"

type Props = {
    selection: string,
    upsList: Ups[]
}

export default function UpsInfoLine(props: Readonly<Props>) {
    const ups: Ups|undefined = props.upsList.find(entry => entry.id === props.selection)
    return (
        <>
        {ups
            ? <p> <a href={"/ups/" + ups.id}> {ups.name} ({ups.address}) </a> </p>
            : <p> None selected </p>
        }
        </>
    )
}