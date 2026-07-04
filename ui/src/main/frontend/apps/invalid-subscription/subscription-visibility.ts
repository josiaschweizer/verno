interface ServerElement extends HTMLElement {
    $server?: Record<string, (...args: unknown[]) => void>;
}

const ATTRIBUTE = 'data-visibility-callback';

document.addEventListener('visibilitychange', () => {
    if (document.visibilityState !== 'visible') {
        return;
    }
    document.querySelectorAll<ServerElement>(`[${ATTRIBUTE}]`).forEach((element) => {
        const callback = element.getAttribute(ATTRIBUTE);
        if (callback) {
            element.$server?.[callback]();
        }
    });
});

export {};