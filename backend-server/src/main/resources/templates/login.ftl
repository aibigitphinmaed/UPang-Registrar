<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <script>
        async function handleLogin(event) {
            event.preventDefault(); // Prevent the default form submission

            const formData = new FormData(event.target);
            const loginRequest = {
                username: formData.get('username'),
                password: formData.get('password')
            };

            const response = await fetch('/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginRequest)
            });

            if (!response.ok) {
                alert('Login failed!');
            }else {
                // If login is successful, redirect to the dashboard
                window.location.href = '/dashboard';
            }
        }
    </script>
</head>
<body>
<h2>Login</h2>
<form onsubmit="handleLogin(event)">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required>
    <br>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required>
    <br>

    <button type="submit">Login</button>
</form>
</body>
</html>