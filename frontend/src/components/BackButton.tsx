type ButtonProps = {
    visible: boolean,
    clickAction: () => void
    text: string
}

export default function (props: Readonly<ButtonProps>) {
    return (
        <>
            {props.visible
                && <button onClick={() => props.clickAction()}>
                    props.text
                </button>}
        </>
    )
}