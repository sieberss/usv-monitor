type BottomProps = {
    resetForm: () => void,
    changedData: boolean,
    submitEditForm: () => void,
    deleteClicked: () => void,
    editing: boolean,
    switchEditMode: (on:boolean) => void,
    message: string,
    confirmationMessage: string
}

export default function FormBottom(props:Readonly<BottomProps>){
    return (
        <>
            <li></li>
    <li>
        <button id={"reset"} type={"button"} onClick={() => {
            props.resetForm()
        }} hidden={!props.changedData}>
            Reset
        </button>
        <button id={"submit"} type={"button"} onClick={() => props.submitEditForm()} hidden={!props.changedData}>
            Save
        </button>
    </li>
    <li>
        <button type={"button"} onClick={props.deleteClicked} hidden={props.editing}>
            Delete
        </button>
        <button type={"button"} onClick={() => props.switchEditMode(true)}
                hidden={props.editing || props.message === props.confirmationMessage}>
            Edit
        </button>
    </li>
    <p className={"message"}>{props.message}</p>
</>
)
}