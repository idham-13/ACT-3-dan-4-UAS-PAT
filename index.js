const express = require("express");
const app = express();
const Sequelize = require('sequelize');
const { sequelize } = require("sequelize/lib/model");
const conn = new Sequelize("db_game", "root", "", {
    host: "localhost",
    dialect: "mysql",
    logging: false
});

const bcrypt = require("bcrypt");

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.listen(3000, function () {
});

const { Model, DataTypes, Op } = Sequelize;
class User extends Model { }
class Menu extends Model { }
class Orderlist extends Model { }

User.init(
    {
        UserId: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        Username: {
            type: DataTypes.STRING,
        },
        Password: {
            type: DataTypes.STRING,
        },
        Nama: {
            type: DataTypes.STRING
        },
        NoHp: {
            type: DataTypes.STRING
        },
        AlamatEmail: {
            type: DataTypes.STRING
        }
    },
    {
        sequelize: conn,
        underscored: true,
        timestamps: false,
        modelName: "User",
        tableName: "user"
    }
)

Menu.init(
    {
        Menu_id: {
            type: DataTypes.INTEGER,
            autoIncrement: true,
            primaryKey: true
        },
        AccountName: {
            type: DataTypes.STRING
        },
        Price: {
            type: DataTypes.STRING
        },
        Deskripsi: {
            type: DataTypes.STRING
        },
        GameName: {
            type: DataTypes.STRING
        }
    },
    {
        sequelize: conn,
        underscored: true,
        timestamps: false,
        modelName: "Menu",
        tableName: "menu"
    }
)

Orderlist.init(
    {
        Order_id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        AccountName: {
            type: DataTypes.STRING
        },
        GameName: {
            type: DataTypes.STRING
        },
        Username: {
            type: DataTypes.STRING
        },
        Status: {
            type: DataTypes.STRING
        }
    },
    {
        sequelize: conn,
        underscored: true,
        timestamps: false,
        modelName: "Orderlist",
        tableName: "orderlist"
    }
)

User.hasMany(Menu);
Menu.belongsTo(User);

// (async () => {
//     await conn.sync({ force: true });
//  })();

app.post("/register", async function (req, res) {
    const { username, password, nama, no_hp, email } = req.body;

    // Check if the username already exists
    let existingUser = await User.findOne({ where: { Username: username } });
    
    if (existingUser) {
        return res.status(400).json({ message: "Username sudah digunakan" });
    }

    let newUser = await User.create({
        Username: username,
        Password: password,
        Nama: nama,
        NoHp: no_hp,
        AlamatEmail: email
    });

    const result = {
        "message": "User berhasil register"
    }
    res.status(201).json(result);
});


app.post("/login", async function (req, res) {
    const { username, password } = req.body;
  
    const cekUser = await User.findOne({
      where: {
        Username: username,
      },
    });
  
    if (!cekUser) {
      const result = {
        message: "User not found",
      };
      res.status(404).json(result);
    } else {
      const match = await bcrypt.compare(password, cekUser.dataValues.Password);
  
      if (!match) {
        const result = {
          message: "Wrong password",
        };
        res.status(400).json(result);
      } else {
        const result = {
          message: "Welcome, " + username,
        };
        res.status(200).json(result);
      }
    }
  });

  app.post('/logout', function (req, res) {
    // Hapus data sesi yang terkait dengan pengguna yang saat ini masuk
    req.session.destroy(function(err) {
        if(err) {
            // Jika terjadi kesalahan saat menghapus sesi
            res.status(500).send({
                status: 500,
                error: "Internal Server Error",
                message: "Logout failed"
            });
        } else {
            // Logout berhasil
            res.status(200).send({
                status: 200,
                message: "Logout successful",
                isAuthenticated: false
            });
        }
    });
});

app.get("/menu", async function (req, res) {
    let menu = await Menu.findAll({
    })
    const menus = menu.map((item) => {
        for(let i = 0; i < 1; i++){
            return item[i];
        }
    })
    const result = {
        menu
    }
    res.status(200).json(result);
})

app.get("/menu/:id", async function (req, res) {
    const { id } = req.params

    let cekMenu = await Menu.findOne({
        where: {
            "Menu_id": id
        }
    })

    if (!cekMenu) {
        const result = {
            "message": "No menu yet"
        }
        res.status(404).json(result);
    }
    else {
        const result = {
            cekMenu
        }
        res.status(200).json(result);
    }
})

app.post("/create_menu", async function (req, res) {
    const { AccountName, Price, Deskripsi, GameName } = req.body;

    let newMenu = await Menu.create({
        AccountName: AccountName,
        Price: Price,
        Deskripsi: Deskripsi,
        GameName: GameName
    })

    const result = {
        "message": "New menu added"
    }
    res.status(201).json(result);
})

app.put("/menu/:id", async function (req, res) {
    const { id } = req.params
    const { AccountName, Price, Deskripsi, GameName } = req.body

    const cekMenu = await Menu.findOne({
        where: {
            Menu_id: id
        }
    })

    if (!cekMenu) {
        const result = {
            "message": "Menu not found"
        }
        res.status(404).json(result);
    }
    else {
        let updateMenu = await Menu.update({
            AccountName: AccountName,
            Price: Price,
            Deskripsi: Deskripsi,
            GameName: GameName
        }, {
            where: {
                Menu_id: id
            }
        })

        const result = {
            "message": "Update success"
        }
        res.status(200).json(result);
    }
})

app.delete("/menu/:id", async function (req, res) {
    const { id } = req.params

    let cekMenu = await Menu.findOne({
        where: {
            Menu_id: id
        }
    })

    if (!cekMenu) {
        const result = {
            "message": "Menu not found"
        }
        res.status(404).json(result);
    }
    else {
        let deleteMenu = await Menu.destroy({
            where: {
                Menu_id: id
            }
        })
        return res.status(200).json({ "message": "Delete Success" })
    }
})

app.post("/order", async function (req, res) {
    const { GameName, AccountName, Username, Status } = req.body;

    let newOrder = await Orderlist.create({
        GameName: GameName,
        AccountName: AccountName,
        Username: Username,
        Status: Status
    })

    return res.status(201).json({ "message": "Order success" })
})

app.get("/order", async function (req, res) {
    let getOrderlist = await Orderlist.findAll()

    const result = {
        getOrderlist
    }
    res.status(200).json(result);
})

app.put("/order/:id", async function (req, res) {
    const { id } = req.params;
    const { Status } = req.body;

    let cekOrder = await Orderlist.findOne({
        where: {
            Order_id: id
        }
    })

    if (!cekOrder) {
        return res.status(404).json({ "message": "Order not found" })
    }
    else {
        let updateStatus = await Orderlist.update({
            Status: Status
        }, {
            where: {
                Order_id: id
            }
        })

        return res.status(200).json({ "message": "Status updated" })
    }
})
