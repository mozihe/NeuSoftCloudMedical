<template>
  <div class="chat-widget">
    <button ref="chatToggle" @click="toggleChat" class="chat-toggle" v-if="!isChatOpen && !isAnimating">
      💬
    </button>
    <div ref="chatBox" class="chat-box" v-if="isChatOpen || isAnimating" @animationend="handleAnimationEnd">
      <div class="chat-header">
        <span>虚拟医生</span>
        <button @click="toggleChat">✖️</button>
      </div>
      <div class="chat-messages">
        <div v-for="(message, index) in messages" :key="index" :class="message.role">
          <div class="message-box">
            <span>{{ message.content }}</span>
          </div>
        </div>
      </div>
      <div class="chat-input">
        <input
            v-model="newMessage"
            @keyup.enter="sendMessage"
            placeholder="输入信息"
            :disabled="isWaitingForReply || newMessage.length > 500"
        />
        <button @click="sendMessage" :disabled="isWaitingForReply || newMessage.length > 500">Send</button>
      </div>
      <div v-if="newMessage.length > 500" class="error-message">太长了，最多500字</div>
    </div>
  </div>
</template>

<script>

import ollama from 'ollama'

export default {

  data() {
    return {
      isChatOpen: false,
      newMessage: '',
      messages: [],
      isAnimating: false,
      isWaitingForReply: false
    };
  },

  methods: {
    toggleChat() {
      if (!this.isAnimating) {
        this.isAnimating = true;
        if (this.isChatOpen) {
          this.$refs.chatBox.classList.remove('slide-in');
          this.$refs.chatBox.classList.add('slide-out');
          setTimeout(() => {
            this.isChatOpen = false;
          }, 500);
        } else {
          this.isChatOpen = true;
          this.$nextTick(() => {
            this.$refs.chatBox.classList.add('slide-in');
          });
        }
      }
    },
    handleAnimationEnd() {
      this.isAnimating = false;
      if (!this.isChatOpen) {
        if (this.$refs.chatToggle) {
          this.$refs.chatToggle.style.display = 'block';
        }
      } else {
        if (this.$refs.chatToggle) {
          this.$refs.chatToggle.style.display = 'none';
        }
      }
      this.$refs.chatBox.classList.remove('slide-in');
      this.$refs.chatBox.classList.remove('slide-out');
    },
    async sendMessage() {
      if (this.newMessage.trim() === '' || this.isWaitingForReply || this.newMessage.length > 500) return;

      const message = {
        "role": 'User',
        "content": this.newMessage
      };
      this.messages.push(message);
      this.newMessage = '';
      this.isWaitingForReply = true;


      try {
        const response = await ollama.chat({
          "model": "mymodel",
          "messages": this.messages,
          "stream": false
        })

        console.log(response);

        if (response.message && response.message.content) {
          this.messages.push({
            "role": 'Assistant',
            "content": response.message.content
          });
          this.isWaitingForReply = false;
        }
      } catch (error) {
        console.error('Error sending message:', error);
      }
    }
  }
};
</script>

<style scoped>
.chat-widget {
  position: fixed;
  bottom: 20px;
  right: 20px;
}

.chat-toggle {
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  cursor: pointer;
  font-size: 24px;
}

.chat-box {
  width: 300px;
  height: 400px;
  border: 1px solid #ccc;
  background-color: white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  transform-origin: bottom right;
}

.chat-box.slide-in {
  animation: slide-in 0.5s forwards;
}

.chat-box.slide-out {
  animation: slide-out 0.5s forwards;
}

@keyframes slide-in {
  from {
    transform: scale(0.8);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

@keyframes slide-out {
  from {
    transform: scale(1);
    opacity: 1;
  }
  to {
    transform: scale(0.8);
    opacity: 0;
  }
}

.chat-header {
  background-color: #007bff;
  color: white;
  padding: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-messages {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
}

.chat-input {
  display: flex;
  border-top: 1px solid #ccc;
  padding: 10px;
}

.chat-input input {
  flex: 1;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 3px;
  font-size: 14px; /* Reduce the font size */
}

.chat-input button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 5px 10px;
  margin-left: 5px;
  cursor: pointer;
  border-radius: 3px;
}

.message-box {
  max-width: 80%;
  padding: 10px;
  margin: 5px 0;
  border-radius: 10px;
  word-wrap: break-word; /* Ensure text wraps */
}

.User .message-box {
  background-color: #dcf8c6;
  align-self: flex-end;
}

.Assistant .message-box {
  background-color: #f1f0f0;
  align-self: flex-start;
}

.error-message {
  color: red;
  font-size: 12px;
  text-align: center;
  padding: 5px;
}
</style>
