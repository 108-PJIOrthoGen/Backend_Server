# Password Recovery Email Configuration

Password recovery sends OTP emails through Spring Mail. If SMTP is not configured,
the backend defaults to `localhost:1025`, which is intended for a local SMTP
catcher such as MailHog or Mailpit. A connection refused error on
`localhost:1025` means no local SMTP server is running.

## Local Development With MailHog

Start a local SMTP catcher:

```bash
docker run --rm -p 1025:1025 -p 8025:8025 mailhog/mailhog
```

Then keep the default backend settings:

```bash
MAIL_HOST=localhost
MAIL_PORT=1025
MAIL_SMTP_AUTH=false
MAIL_SMTP_STARTTLS_ENABLE=false
PASSWORD_RECOVERY_MAIL_FROM=no-reply@pji.local
```

Open `http://localhost:8025` to view OTP emails.

## Gmail SMTP

Gmail requires a Google App Password. A normal Google account password will not
work.

```bash
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-gmail-address@gmail.com
MAIL_PASSWORD=your-google-app-password
MAIL_SMTP_AUTH=true
MAIL_SMTP_STARTTLS_ENABLE=true
PASSWORD_RECOVERY_MAIL_FROM=your-gmail-address@gmail.com
```

Restart the backend after changing these variables.

## Docker Deployment

The production docker compose file requires the same SMTP variables for the
`pji-backend` service. Add them to the deployment `.env` file before starting the
stack.
