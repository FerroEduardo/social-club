const siteKey = import.meta.env.VITE_RECAPTCHA_KEY;

async function load() {
  if (!document.getElementById('recaptcha-js')) {
    const script = document.createElement('script');
    script.id = 'recaptcha-js';
    script.src = `https://www.google.com/recaptcha/api.js?render=${siteKey}`;

    document.body.append(script);
  }
}

async function execute(): Promise<string> {
  const grecaptcha: any = window.grecaptcha;
  if (!grecaptcha) {
    alert('Failed to load reCAPTCHA');
    return Promise.reject('Failed to load reCAPTCHA');
  }

  return new Promise((resolve) => {
    grecaptcha.ready(async function () {
      grecaptcha.execute(siteKey, { action: 'submit' }).then((token: string) => {
        return resolve(token);
      });
    });
  });
}

export default {
  load,
  execute
};
