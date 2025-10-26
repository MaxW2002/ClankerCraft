# ClankerCraft
<img width="2100" height="1080" alt="banna2" src="https://github.com/user-attachments/assets/6cc5c323-70b5-4882-93ab-b5b1033a9b9b" />
A Minecraft Fabric mod that brings AI-powered mobs and creative tools to your world. Chat with friendly companions, generate custom paintings, and create music—all powered by Google's AI services.

---

## Components

### The Clanker Mob
A friendly companion mob that players can talk to. Based on the Copper Golem with all hostile behaviors removed. Spawn using the Clanker spawn egg.

### Chat System
Start conversations by typing `@clanker` in chat. The mob responds using AI and remembers your conversation. End with `@byebye`. Each player gets their own conversation session, so multiple players can chat with different Clankers at once.

### AI Conversations
Powered by Google's Gemini language model. Messages are sent to the Gemini API with conversation history, and the model generates natural responses. Customize the Clanker's personality using text files—choose from Excited, Grumpy, or Robotic, or create your own.

### Text-to-Speech
Clanker's responses are spoken aloud using Google Cloud Text-to-Speech with Chirp 3 HD voices. Audio plays positionally in 3D space, so you hear the voice coming from the Clanker's location.

### Image Generation
Create custom paintings by typing `@makepainting <prompt>` during a conversation. Uses Vertex AI's Imagen model to generate images from your description. The image becomes a painting texture in your world.

### Music Generation
Generate music discs by typing `@makemusic <prompt>`. Uses Vertex AI's Lyria 2 model to create music based on your description. The audio is transcoded to OGG format (requires FFmpeg) and saved as a playable music disc.

---

## Work Division

This project was developed collaboratively by **MaxW2002** and **zebby09**.

### MaxW2002
- Initial project setup and repository structure
- Core chat system and AI integration with Gemini
- Text-to-Speech implementation using Google Cloud TTS
- Image generation system with Vertex AI Imagen
- Music generation system with Vertex AI Lyria 2
- Personality system implementation
- Video editing

### zebby09
- Clanker follow and stay commands
- In-game personality switching functionality
- Clanker self-awareness features
- Textures for clanker and clanker egg
- Multi-language translations and localization
- Voice configuration improvements
- Video editing

---

## AI and Cloud Services

ClankerCraft uses the following Google AI services:

- **Gemini** (Google AI Studio) — Powers natural language conversations
- **Google Cloud Text-to-Speech** — Converts text to spoken audio with Chirp 3 HD voices
- **Vertex AI Imagen** — Generates images for custom paintings
- **Vertex AI Lyria 2** — Creates music for custom discs

You'll need API keys for these services. All keys are configured in a single properties file.

---

## Setup Guide

### Prerequisites
- Java 21
- Minecraft with Fabric Loader and Fabric API installed
- FFmpeg installed and available in your system PATH (required for music generation)

### Step 1: Find Your Config File
1. Find the config file: `clankercraft.properties` in the `run/config` folder in this repository
2. Open the file in a text editor

### Step 2: Get API Keys

**For Chat**
1. Go to [Google AI Studio](https://aistudio.google.com/)
2. Create an API key
3. Add it to your config file:
   ```
   GOOGLE_AI_STUDIO_API_KEY=your_key_here
   ```

**Google Cloud**
1. Create a new google cloud project
2. Go to _project info_ and add the _Project ID_ to your config file:
   ```
   GOOGLE_CLOUD_PROJECT_ID=your_project-id_here
   ```
3. Next, in the Google Cloud console go to _API's and Services_ and then _Credentials_
4. Click _Create credentials_ and set up an API key for your Google Cloud project
5. Once created, paste your Google Cloud API key in the config file:
   ```
   GOOGLE_CLOUD_API_KEY=your_key_here
   ```

**For TTS**
1. In the Google Cloud console go to _API's and Services_ 
2. Under _Enabled APIs & Services_, enable the _Cloud Text-to-Speech API_ 

**For Images**
1. In the Google Cloud console go to _API's and Services_ 
2. Under _Enabled APIs & Services_, enable the _Vertex AI API_ 

**For Music**
1. Make sure the _Vertex AI API_ is enabled in your Google Cloud project
2. Make sure **_FFMPEG_** is installed on your PATH (this is used for audio conversion)

**Service Account**
1. Next, in the Google Cloud console go to _API's and Services_ and then _Credentials_
2. Click _Create credentials_ and create a _Service account_
3. Under _Permissions_ select the following roles to this service account:
    - _Service Usage Consumer_
    - _Storage Object Viewer_
    - _Vertex AI User_
4. Next, in the Google Cloud console go to _IAM & Admin_ and then _Service Accounts_
5. Open your created service account and go to _keys_
6. Create a new key (JSON file) and store it locally on your PC
7. Once saved, paste the path to this local JSON file in the config file:
   ```
   GOOGLE_APPLICATION_CREDENTIALS=/path/to/your/credentials.json
   ```


### Step 3: Further configuration settings
- Optionally configure gemini version to preferred model (_e.g. gemini-2.5-flash_). The gemini model already provided in the config file (_1.5-flash_) gives the best performance relative to its cost.
- Optionally change the style of the TTS voice under '**TTS_VOICE_STYLE**'. Check out the Google Cloud documentation on available Chirp 3 HD voices (_https://cloud.google.com/text-to-speech/docs/chirp3-hd_).

Example for changing the default voice to male:
```
TTS_VOICE_STYLE=D
```
  
- Optionally change the personality of the clanker. Currently base supported personalities include:
     - Excited 
     - Grumpy
     - Robotic

Example for changing the default personality to grumpy:
```
CLANKER_PERSONALITY=Grumpy
```

- Optionally change the language of ClankerCraft, which will adapt the Clanker's language. Currently supported languages include:
     - Dutch (_nl_)
     - English (_en_)
     - German (_de_)
     - Spanish (_es_)
     - French (_fr_)
     - Italian (_it_)
     - Japanese (_ja_)
     - Korean (_ko_)
     - Portuguese (_pt_)
     - Chinese Mandarin (_zh_)

       in order to use any of these languages simply change the variable for '**CLANKER_LANGUAGE**' to the given tag for the languages above.

Example for changing the default language to Japanese:
     
```
CLANKER_LANGUAGE=ja
```


### Step 4: Launch Minecraft
1. Start Minecraft with the ClankerCraft mod installed
2. Check the console for confirmation messages about enabled features

### Step 5: Test the Features

**Test Chat:**
- Give yourself a Clanker spawn egg: `/give @s clankercraft:clanker_spawn_egg`
- Spawn a Clanker
- Type `@clanker hello` in chat
- You should see a response

**Test TTS:**
- If configured, you'll hear the Clanker speak its responses

**Test Paintings:**
- Start a conversation with `@clanker`
- Type `@makepainting a sunset over mountains`
- Wait for the success message
- Press `F3 + T` to reload textures
- Place a painting to see your generated image

**Test Music:**
- Start a conversation with `@clanker`
- Type `@makemusic upbeat electronic dance`
- Wait for the success message
- Pick up the generated disc
- Place it in a jukebox to play

---
