import './FormBottom.css'

type BottomProps = {
    resetForm: () => void,
    changedData: boolean,
    submitEditForm: () => void,
    deleteClicked: () => void,
    editing: boolean,
    switchEditMode: (on: boolean) => void,
    message: string,
    confirmationMessage: string
}

export default function FormBottom(props: Readonly<BottomProps>) {
    return (
        <>
            <li className={"subline"}></li>
            <li className={"subline"}>
                <button id={"reset"} type={"button"} onClick={() => {
                    props.resetForm()
                }} hidden={!props.changedData}>
                    Reset
                </button>
                <button id={"submit"} type={"button"} onClick={() => props.submitEditForm()}
                        hidden={!props.changedData}>
                    Save
                </button>
            </li>
            <li className={"subline"}>
                <button type={"button"} onClick={props.deleteClicked} hidden={props.editing}>
                    Delete
                </button>
                <button type={"button"} onClick={() => props.switchEditMode(true)}
                        hidden={props.editing}>
                    Edit
                </button>
            </li>
            <li className={"subline"}> <p className={"message"}>{props.message}</p></li>
        </>
    )
}