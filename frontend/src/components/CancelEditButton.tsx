type ButtonProps = {
    visible:boolean,
    clickAction: () => void
}

export default function (props:Readonly<ButtonProps>){
    return(
        <>
            {props.visible &&
                <button onClick={() => props.clickAction()}>
                    Abbrechen
                </button>}
        </>
    )
}