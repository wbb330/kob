<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <img :src="$store.state.user.photo" alt="" style="width: 100%;">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-header">
                        <span style="font-size: 130%; margin-left: 1%;">My Bot</span>
                        <button type="button" class="btn btn-primary float-end" style="margin-right: 5px;" data-bs-toggle="modal" data-bs-target="#addBotButton">创建Bot</button>
                        <!-- 创建Bot的modal -->
                        <div class="modal fade" id="addBotButton" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="exampleModalLabel">创建Bot</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="addBotTitle" class="form-label">Bot名称</label>
                                            <input v-model="botAddMessage.title" type="email" class="form-control" id="addBotTitle" placeholder="请输入Bot名称" autocomplete="off">
                                        </div>
                                        <div class="mb-3">
                                            <label for="addBotDescription" class="form-label">Bot简介</label>
                                            <textarea v-model="botAddMessage.description" class="form-control" id="addBotDescription" rows="3" placeholder="请输入Bot简介"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="addBotContent" class="form-label">Bot代码</label>
                                            <VAceEditor
                                                v-model:value="botAddMessage.content"
                                                @init="editorInit"
                                                lang="c_cpp"
                                                theme="textmate"
                                                style="height: 300px" />
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <div class="error-message">{{ botAddMessage.error_message }}</div>
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                                        <button type="button" class="btn btn-primary" @click="add_bot">提交</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>名称</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createdTime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-outline-secondary" style="margin-right: 10px;"  data-bs-toggle="modal" :data-bs-target="'#updateBotModal_' + bot.id">修改</button>
                                        <button type="button" class="btn btn-outline-danger" @click="remove_bot(bot)">删除</button>

                                        <!-- 创建Bot的modal -->
                                        <div class="modal fade" :id="'updateBotModal_' + bot.id" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">修改Bot</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="mb-3">
                                                            <label for="addBotTitle" class="form-label">Bot名称</label>
                                                            <input v-model="bot.title" type="email" class="form-control" id="addBotTitle" autocomplete="off">
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="addBotDescription" class="form-label">Bot简介</label>
                                                            <textarea v-model="bot.description" class="form-control" id="addBotDescription" rows="3"></textarea>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="addBotContent" class="form-label">Bot代码</label>
                                                            <VAceEditor
                                                                v-model:value="bot.content"
                                                                @init="editorInit"
                                                                lang="c_cpp"
                                                                theme="textmate"
                                                                style="height: 300px" />

                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <div class="error-message">{{ bot.error_message }}</div>
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                                                        <button type="button" class="btn btn-primary" @click="update_bot(bot)">保存修改</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import $ from 'jquery'
    import { useStore } from 'vuex'
    import { ref, reactive } from 'vue'
    import { Modal } from 'bootstrap/dist/js/bootstrap'
    import { VAceEditor } from 'vue3-ace-editor';
    import ace from 'ace-builds';


    export default {
        components: {
            VAceEditor
        },
        setup() {
            ace.config.set(
            "basePath", 
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

            const store = useStore();
            let bots = ref([]);
            const botAddMessage = reactive({
                title: "",
                description: "",
                content: "",
                error_message: ""
            })

            const refresh_bots = () => {
                $.ajax({
                    url: "http://127.0.0.1:3000/user/bot/search",
                    type: "get",
                    headers: {
                        Authorization: "Bearer " + store.state.user.token,
                    },
                    success(resp) {
                        bots.value = resp;
                    }
                })
            }
            refresh_bots();
            const add_bot = () => {
                botAddMessage.error_message = "";
                $.ajax({
                    url: "http://127.0.0.1:3000/user/bot/add",
                    type: "post",
                    data: {
                        title: botAddMessage.title,
                        description: botAddMessage.description,
                        content: botAddMessage.content,
                    },
                    headers: {
                        Authorization: "Bearer " + store.state.user.token,
                    },
                    success(resp) {
                        if (resp.error_message === "success") {
                            botAddMessage.title = "";
                            botAddMessage.description = "";
                            botAddMessage.content = "";
                            Modal.getInstance("#addBotButton").hide();
                            refresh_bots();
                        } else {
                            botAddMessage.error_message = resp.error_message;
                        }
                    }
                })
            }
            const remove_bot = (bot) => {
                $.ajax({
                    url: "http://127.0.0.1:3000/user/bot/remove",
                    type: "post",
                    data: {
                        botId: bot.id,
                    },
                    headers: {
                        Authorization: "Bearer " + store.state.user.token,
                    },
                    success(resp) {
                        if (resp.error_message === "success") {
                            refresh_bots();
                        }
                    }
                })
            }
            const update_bot = (bot) => {
                botAddMessage.error_message = "";
                $.ajax({
                    url: "http://127.0.0.1:3000/user/bot/update",
                    type: "post",
                    data: {
                        botId: bot.id,
                        title: bot.title,
                        description: bot.description,
                        content: bot.content,
                    },
                    headers: {
                        Authorization: "Bearer " + store.state.user.token,
                    },
                    success(resp) {
                        if (resp.error_message === "success") {
                            Modal.getInstance('#updateBotModal_' + bot.id).hide();
                            refresh_bots();
                        } else {
                            botAddMessage.error_message = resp.error_message;
                        }
                    }
                })
            }

            return {
                bots,
                botAddMessage,
                add_bot,
                remove_bot,
                update_bot,
            }
        }
    }
</script>

<style scoped>
    div.error-message {
        color: red;
    }
</style>